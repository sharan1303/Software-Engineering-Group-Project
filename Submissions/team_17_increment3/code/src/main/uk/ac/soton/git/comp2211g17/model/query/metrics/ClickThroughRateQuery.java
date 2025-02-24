package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.PercentageField;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;
import static uk.ac.soton.git.comp2211g17.generated.Tables.IMPR;

public class ClickThroughRateQuery extends KeyMetricQueryBase {
	public static final PercentageField ctr = new PercentageField("Click Through Rate");

	private final DatabaseManager dbm;

	public ClickThroughRateQuery(DatabaseManager dbm) {
		super(ctr);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);

		if (getInterval() > 0) {
			Table<Record2<Integer, Integer>> imprCounts = create
				.select(epoch(IMPR.DATE).div(getInterval()).as("divTime"), count(asterisk()).cast(SQLDataType.INTEGER).as("countImpr"))
				.from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).groupBy(field("divTime")).asTable();

			Table<Record2<Integer, Integer>> clickCounts = create
				.select(epoch(CLICK.DATE).div(getInterval()).as("divTime"), count(asterisk()).cast(SQLDataType.INTEGER).as("countClick"))
				.from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)).groupBy(field("divTime")).asTable();

			// Left join on impression count - this excludes values where there are clicks but no impressions, but those
			// would return infinite/invalid values anyway

			Field<Integer> time = field("divTime").cast(SQLDataType.INTEGER).mul(getInterval());

			Result<Record2<Integer, Float>> result = create
				.select(time,
					// When no value is available for click count, replace with 0
					coalesce(clickCounts.field("countClick").cast(SQLDataType.REAL), 0F)
						.divide(imprCounts.field("countImpr").cast(SQLDataType.REAL)))
				.from(imprCounts.leftJoin(clickCounts).using(field("divTime"))).fetch();

			return getResults(result);
		} else {
			var a = count(asterisk()).cast(SQLDataType.REAL).as("a");
			var b = count(asterisk()).cast(SQLDataType.REAL).as("b");
			Result<Record1<Float>> result = create.select(
				a.divide(b)
			).from(
				create.select(a).from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)),
				create.select(b).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR))
			).fetch();

			return getResults(result);
		}
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(copyTo(new ClickThroughRateQuery(dbm))::execute);
	}
}
