package uk.ac.soton.git.comp2211g17.model.inputs;

import uk.ac.soton.git.comp2211g17.model.query.impl.DatabaseManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * InputDataParser - parses a CSV-like input and allows interaction with the dataset
 *
 * @author Konrad Sobczak
 * @version 1.0
 * @since 1.0
 */
public class InputDataParser {
	private DatabaseManager dbm;

	public static String[] FIELDS = new String[]{
		"DATE",
		"DATE-ENTRY",
		"DATE-EXIT",
		"ID",
		"USER-GENDER",
		"USER-AGE",
		"INCOME",
		"CONTEXT",
		"IMPRESSION-COST",
		"VIEWED-PAGES",
		"CONVERSION",
		"CLICK-COST"
	};

	public static class Entry {
		private final HashMap<String, String> values = new HashMap<>();

		public Entry(String[] layout, String[] data) {
			for (int i = 0; i < layout.length; i++) {
				if (data.length >= i) {
					values.put(layout[i], data[i]);
				} else {
					values.put(layout[i], null);
				}
			}
		}

		public String get(String key) {
			if (this.values.containsKey(key)) {
				return this.values.get(key);
			} else {
				return null;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		DatabaseManager dbm = new DatabaseManager();
		InputDataParser parser = new InputDataParser(dbm, new BufferedReader(new FileReader("click_log.csv")),
			"DATE,ID,CLICK-COST", true);
		InputDataParser parser2 = new InputDataParser(dbm, new BufferedReader(new FileReader("impression_log.csv")),
			"DATE,ID,USER-GENDER,USER-AGE,INCOME,CONTEXT,IMPRESSION-COST", true);
		InputDataParser parser3 = new InputDataParser(dbm, new BufferedReader(new FileReader("server_log.csv")),
			"DATE,ID,DATE-EXIT,VIEWED-PAGES,CONVERSION", true);
	}

	public InputDataParser(DatabaseManager dbm, BufferedReader reader, String format, boolean skipFirst) throws IOException {
		if (skipFirst) {
			reader.readLine(); //gets rid of the first line
		}

		this.dbm = dbm;

		String[] layout = format.split("([,;\\t])");

		if (Arrays.asList(layout).containsAll(Arrays.asList("CLICK-COST", "DATE"))) {
			ArrayList<DatabaseManager.ClickLogEntry> entries = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("([,;\\t])");
				Entry entry = new Entry(layout, data);

				DatabaseManager.ClickLogEntry clickLogEntry = new DatabaseManager.ClickLogEntry(
					entry.get("DATE"),
					Long.parseLong(entry.get("ID")),
					Double.parseDouble(entry.get("CLICK-COST"))
				);
				entries.add(clickLogEntry);
			}

			dbm.batchInsertClickLog(entries);
		}

		if (Arrays.asList(layout).containsAll(Arrays.asList("IMPRESSION-COST"))) {
			ArrayList<DatabaseManager.ImpressionLogEntry> entries = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("([,;\\t])");
				Entry entry = new Entry(layout, data);

				DatabaseManager.ImpressionLogEntry impressionLogEntry = new DatabaseManager.ImpressionLogEntry(
					entry.get("DATE"), Long.parseLong(entry.get("ID")), entry.get("USER-AGE"), entry.get("USER-GENDER"), entry.get("INCOME"), entry.get("CONTEXT"), Double.parseDouble(entry.get("IMPRESSION-COST"))
				);
				entries.add(impressionLogEntry);
			}

			dbm.batchInsertImpressionLog(entries);
		}

		if (Arrays.asList(layout).containsAll(Arrays.asList("DATE", "DATE-EXIT", "VIEWED-PAGES"))) {
			ArrayList<DatabaseManager.ServerLogEntry> entries = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("([,;\\t])");
				Entry entry = new Entry(layout, data);

				boolean wasConversion = entry.get("CONVERSION").equals("Yes");

				DatabaseManager.ServerLogEntry serverLogEntry = new DatabaseManager.ServerLogEntry(
					entry.get("DATE"), Long.parseLong(entry.get("ID")), entry.get("DATE-EXIT"), Integer.parseInt(entry.get("VIEWED-PAGES")), wasConversion
				);
				entries.add(serverLogEntry);
			}

			dbm.batchInsertServerLog(entries);
		}

	}
}