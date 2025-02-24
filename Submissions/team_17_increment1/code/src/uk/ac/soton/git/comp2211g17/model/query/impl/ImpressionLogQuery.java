package uk.ac.soton.git.comp2211g17.model.query.impl;

import uk.ac.soton.git.comp2211g17.model.query.PaginatableQuery;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;
import uk.ac.soton.git.comp2211g17.model.types.field.ContinuousNumericField;
import uk.ac.soton.git.comp2211g17.model.types.field.DateTimeField;
import uk.ac.soton.git.comp2211g17.model.types.field.IDField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A query for the raw Impression Log data, with pagination support
 */
public class ImpressionLogQuery implements Query, PaginatableQuery {
	public static final DateTimeField date = new DateTimeField("Date");
	public static final IDField id = new IDField("ID");
	public static final CategoriesField<Gender> gender = new CategoriesField<>("Gender", Gender.values());
	public static final CategoriesField<AudienceAge> audienceAge = new CategoriesField<>("Age", AudienceAge.values());
	public static final CategoriesField<IncomeGroup> incomeGroup = new CategoriesField<>("Income", IncomeGroup.values());
	public static final CategoriesField<Context> context = new CategoriesField<>("Context", Context.values());
	public static final ContinuousNumericField impressionCost = new ContinuousNumericField("Impression Cost");

	private int limit = 100;
	private int offset = 0;
	private final DatabaseManager dbm;

	public ImpressionLogQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	public Column<?>[] execute() throws SQLException {
		List<LocalDateTime> dateData = new ArrayList<>();
		List<Long> idData = new ArrayList<>();
		List<Gender> genderData = new ArrayList<>();
		List<AudienceAge> audienceAgeData = new ArrayList<>();
		List<IncomeGroup> incomeGroupData = new ArrayList<>();
		List<Context> contextData = new ArrayList<>();
		List<Float> impressionCostData = new ArrayList<>();

		try (Statement stmt = dbm.conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT * FROM impr LIMIT " + limit + " OFFSET " + offset + ";")) {
			while (rs.next()) {
				dateData.add(LocalDateTime.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				idData.add(rs.getLong("id"));
				audienceAgeData.add(AudienceAge.getFromSQLName(rs.getString("age")));
				genderData.add(Gender.getFromSQLName(rs.getString("gender")));
				incomeGroupData.add(IncomeGroup.getFromSQLName(rs.getString("income")));
				contextData.add(Context.getFromSQLName(rs.getString("context")));
				impressionCostData.add(rs.getFloat("impressionCost"));
			}
		}

		return new Column<?>[]{
			date.makeColumn(dateData.toArray(new LocalDateTime[0])),
			id.makeColumn(idData.stream().mapToLong(i -> i).toArray()),
			gender.makeColumn(genderData.toArray(new Gender[0])),
			audienceAge.makeColumn(audienceAgeData.toArray(new AudienceAge[0])),
			incomeGroup.makeColumn(incomeGroupData.toArray(new IncomeGroup[0])),
			context.makeColumn(contextData.toArray(new Context[0])),
			impressionCost.makeColumn(QueryUtils.toFloatArray(impressionCostData))
		};
	}

	// TODO: these could be abstracted into a higher level query class?
	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
