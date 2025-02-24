package uk.ac.soton.git.comp2211g17.model.inputs;

import org.apache.commons.cli.*;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;

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
	private static final String[] FIELDS = new String[]{
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

	/**
	 * InputDataParser.Entry - represents a single parsed entry
	 *
	 * @author Konrad Sobczak
	 * @version 1.0
	 * @since 1.0
	 */
	public static class Entry {
		private final HashMap<String, String> values = new HashMap<>();

		/**
		 * Construct a new Entry with given layout and data
		 *
		 * @param layout Array of the data layout (format string)
		 * @param data Array of the extracted data
		 */
		public Entry(String[] layout, String[] data) {
			for (int i = 0; i < layout.length; i++) {
				if (data.length >= i) {
					values.put(layout[i], data[i]);
				} else {
					values.put(layout[i], null);
				}
			}
		}

		/**
		 * Get a value for a key
		 *
		 * @param key required key
		 * @return value of the key or null if does not exist
		 */
		public String get(String key) {
			return this.values.getOrDefault(key, null);
		}
	}

	public static String[] getFields() {
		return FIELDS;
	}

	/**
	 * Import files into the database
	 *
	 * @param args arguments given to the process
	 * @throws IOException when a file cannot be read
	 */
	public static void main(String[] args) throws IOException {
		Options options = new Options();

		Option clf = new Option("c", "click-log", true, "click log file path");
		options.addOption(clf);
		Option ilf = new Option("i", "impression-log", true, "impression log file path");
		options.addOption(ilf);
		Option slf = new Option("s", "server-log", true, "server log file path");
		options.addOption(slf);

		Option file = new Option("f", "file", true, "file path, layout string and first line skipping.");
		file.setArgs(3);
		options.addOption(file);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);

			DatabaseManager dbm = new DatabaseManager();

			if (cmd.getOptionValues("f") != null){
				for (int i = 0; i < cmd.getOptionValues("f").length; i += 3) {
					String path = cmd.getOptionValues("f")[i];
					String layout = cmd.getOptionValues("f")[i + 1];
					boolean sfl = cmd.getOptionValues("f")[i + 2].equals("true");

					InputDataParser.parse(dbm, new BufferedReader(new FileReader(path)),
							layout, sfl);
				}
			}

			if (cmd.getOptionValue("c") != null) {
				String path = cmd.getOptionValue("c");

				InputDataParser.parse(dbm, new BufferedReader(new FileReader(path)),
						"DATE,ID,CLICK-COST", true);
			}

			if (cmd.getOptionValue("i") != null) {
				String path = cmd.getOptionValue("i");

				InputDataParser.parse(dbm, new BufferedReader(new FileReader(path)),
						"DATE,ID,USER-GENDER,USER-AGE,INCOME,CONTEXT,IMPRESSION-COST", true);
			}

			if (cmd.getOptionValue("s") != null) {
				String path = cmd.getOptionValue("s");

				InputDataParser.parse(dbm, new BufferedReader(new FileReader(path)),
						"DATE,ID,DATE-EXIT,VIEWED-PAGES,CONVERSION", true);
			}

			if (cmd.getOptionValues("f") == null && cmd.getOptionValue("c") == null && cmd.getOptionValue("i") == null && cmd.getOptionValue("s") == null) {
				InputDataParser.parse(dbm, new BufferedReader(new FileReader("click_log.csv")),
						"DATE,ID,CLICK-COST", true);

				InputDataParser.parse(dbm, new BufferedReader(new FileReader("impression_log.csv")),
						"DATE,ID,USER-GENDER,USER-AGE,INCOME,CONTEXT,IMPRESSION-COST", true);

				InputDataParser.parse(dbm, new BufferedReader(new FileReader("server_log.csv")),
						"DATE,ID,DATE-EXIT,VIEWED-PAGES,CONVERSION", true);
			}
		} catch (ParseException e) {
			System.err.print("An error has occurred during the parsing of command-line arguments: ");
			System.err.println(e.getMessage());
			formatter.printHelp("input-parser", options);

			System.exit(1);
		}
	}

	/**
	 * Construct an InputDataParser and insert data into the database using specified controller
	 *
	 * @param dbm DatabaseManager used for inserting data
	 * @param reader BufferedReader used for input data
	 * @param format Layout/Format string
	 * @param skipFirst True if first line contains a title or should otherwise be skipped
	 *
	 * @throws IOException when the BufferedReader cannot be accessed properly
	 */
	public static void parse(DatabaseManager dbm, BufferedReader reader, String format, boolean skipFirst) throws IOException {
		if (skipFirst) {
			reader.readLine(); //gets rid of the first line
		}

		String[] layout = format.split("([,;\\t])");

		ArrayList<DatabaseManager.ClickLogEntry> cl_entries = new ArrayList<>();
		ArrayList<DatabaseManager.ImpressionLogEntry> il_entries = new ArrayList<>();
		ArrayList<DatabaseManager.ServerLogEntry> sl_entries = new ArrayList<>();

		String line;

		boolean contains_cl = Arrays.asList(layout).containsAll(Arrays.asList("CLICK-COST", "DATE"));
		boolean contains_il = Arrays.asList(layout).contains("IMPRESSION-COST");
		boolean contains_sl = Arrays.asList(layout).containsAll(Arrays.asList("DATE", "DATE-EXIT", "VIEWED-PAGES"));

		while ((line = reader.readLine()) != null) {
			String[] data = line.split("([,;\\t])");
			Entry entry = new Entry(layout, data);

			if (contains_cl) {
				DatabaseManager.ClickLogEntry clickLogEntry = new DatabaseManager.ClickLogEntry(
						entry.get("DATE"),
						Long.parseLong(entry.get("ID")),
						Double.parseDouble(entry.get("CLICK-COST"))
				);

				cl_entries.add(clickLogEntry);

			}

			if (contains_il) {
				DatabaseManager.ImpressionLogEntry impressionLogEntry = new DatabaseManager.ImpressionLogEntry(
						entry.get("DATE"), Long.parseLong(entry.get("ID")), entry.get("USER-AGE"), entry.get("USER-GENDER"), entry.get("INCOME"), entry.get("CONTEXT"), Double.parseDouble(entry.get("IMPRESSION-COST"))
				);

				il_entries.add(impressionLogEntry);
			}

			if (contains_sl) {
				boolean wasConversion = entry.get("CONVERSION").equals("Yes");
				DatabaseManager.ServerLogEntry serverLogEntry = new DatabaseManager.ServerLogEntry(
						entry.get("DATE"), Long.parseLong(entry.get("ID")), entry.get("DATE-EXIT"), Integer.parseInt(entry.get("VIEWED-PAGES")), wasConversion
				);

				sl_entries.add(serverLogEntry);
			}
		}

		if (contains_cl) {
			dbm.batchInsertClickLog(cl_entries);
		}

		if (contains_il) {
			dbm.batchInsertImpressionLog(il_entries);
		}

		if (contains_sl) {
			dbm.batchInsertServerLog(sl_entries);
		}
	}
}