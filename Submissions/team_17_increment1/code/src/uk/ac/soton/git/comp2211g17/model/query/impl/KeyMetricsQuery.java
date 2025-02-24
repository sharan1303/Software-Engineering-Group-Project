package uk.ac.soton.git.comp2211g17.model.query.impl;

import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.ContinuousNumericField;
import uk.ac.soton.git.comp2211g17.model.types.field.LongIntegerField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KeyMetricsQuery implements Query {
	public static final ContinuousNumericField maxClickCost = new ContinuousNumericField("Maximum Click Cost");
	public static final ContinuousNumericField avgClickCost = new ContinuousNumericField("Mean Click Cost");
	public static final ContinuousNumericField minClickCost = new ContinuousNumericField("Minimum Click Cost");
	public static final LongIntegerField maxFrequency = new LongIntegerField("Maximum Click Cost Frequency (p)");
	public static final LongIntegerField minFrequency = new LongIntegerField("Minimum Click Cost Frequency (p)");
	public static final LongIntegerField medianFrequency = new LongIntegerField("Median Click Cost Frequency (p)");

	private final DatabaseManager dbm;

	public KeyMetricsQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	public Column<?>[] execute() throws SQLException {
		float maxClickCostD;
		float avgClickCostD;
		float minClickCostD;
		long maxFrequencyD = 0;
		long minFrequencyD = 0;
		long medianFrequencyD;

		try (Statement stmt = dbm.conn.createStatement();
			 ResultSet rs = stmt.executeQuery("Select max(clickCost), avg(clickCost), min(clickCost) from click order by clickCost;")) {
			rs.next();
			maxClickCostD = rs.getFloat("max(clickCost)");
			avgClickCostD = rs.getFloat("avg(clickCost)");
			minClickCostD = rs.getFloat("min(clickCost)");
		}
		try (Statement stmt = dbm.conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT floor(clickCost) as range, count(floor(clickCost)) as frequency\n" +
				 "from click\n" +
				 "where clickCost != 0\n" +
				 "group by floor(clickCost)\n" +
				 "order by count(floor(clickCost));")) {
			if (rs.next()) {
				minFrequencyD = rs.getLong("range");
				maxFrequencyD = minFrequencyD;
			}
			while (rs.next()) {
				maxFrequencyD = rs.getLong("range");
			}
		}

		List<Long> rangeData = new ArrayList<>();
		List<Integer> freqData = new ArrayList<>();

		try (Statement stmt = dbm.conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT floor(clickCost) as range, count(floor(clickCost)) as frequency\n" +
				 "from click\n" +
				 "where clickCost != 0\n" +
				 "group by floor(clickCost)\n" +
				 "order by count(floor(clickCost));")) {
			while (rs.next()) {
				rangeData.add(rs.getLong("range"));
				freqData.add(rs.getInt("frequency"));
			}
		}
		int medianIndex = QueryUtils.getMedian(freqData.stream().mapToInt(i -> i).toArray());
		medianFrequencyD = rangeData.get(medianIndex);

		return new Column<?>[]{
			maxClickCost.makeColumn(new float[]{maxClickCostD}),
			avgClickCost.makeColumn(new float[]{avgClickCostD}),
			minClickCost.makeColumn(new float[]{minClickCostD}),
			minFrequency.makeColumn(new long[]{minFrequencyD}),
			medianFrequency.makeColumn(new long[]{medianFrequencyD}),
			maxFrequency.makeColumn(new long[]{maxFrequencyD})
		};
	}
}
