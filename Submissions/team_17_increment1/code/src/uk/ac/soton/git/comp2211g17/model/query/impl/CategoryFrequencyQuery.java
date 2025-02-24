package uk.ac.soton.git.comp2211g17.model.query.impl;

import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;
import uk.ac.soton.git.comp2211g17.model.types.field.LongIntegerField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A query for category frequencies in impressions
 */
public class CategoryFrequencyQuery implements Query {
	public static final CategoriesField<Context> context = new CategoriesField<>("Context", Context.values());
	public static final LongIntegerField frequency = new LongIntegerField("Frequency");

	private final DatabaseManager dbm;

	public CategoryFrequencyQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	public Column<?>[] execute() throws SQLException {
		List<Context> contextData = new ArrayList<>();
		List<Long> freqData = new ArrayList<>();

		try (Statement stmt = dbm.conn.createStatement();
			 ResultSet rs = stmt.executeQuery("select context, count(context) as freq from impr group by context;")) {
			while (rs.next()) {
				contextData.add(Context.getFromSQLName(rs.getString("context")));
				freqData.add(rs.getLong("freq"));
			}
		}

		return new Column<?>[]{
			context.makeColumn(contextData.toArray(new Context[0])),
			frequency.makeColumn(freqData.stream().mapToLong(i -> i).toArray())
		};
	}
}
