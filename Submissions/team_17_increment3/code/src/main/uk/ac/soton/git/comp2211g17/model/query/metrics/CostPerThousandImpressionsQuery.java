package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.CurrencyField;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;
import static uk.ac.soton.git.comp2211g17.generated.Tables.IMPR;

public class CostPerThousandImpressionsQuery extends KeyMetricQueryBase {
	public static final CurrencyField cost = new CurrencyField("Cost Per 1000 Impressions (CPM)");

	private final DatabaseManager dbm;

	public CostPerThousandImpressionsQuery(DatabaseManager dbm) {
		super(cost);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		if (getInterval() > 0) {
			// Table of divided timestamps and the total cost per interval
			Table<Record2<Integer, Integer>> costs =
				create.select(
					field("divTime").cast(SQLDataType.INTEGER).as("divTime"),
					sum(field("cost").cast(SQLDataType.INTEGER)).cast(SQLDataType.INTEGER).as("cost")
				).from(
					create.select(epoch(CLICK.DATE).div(getInterval()).as("divTime"), sum(CLICK.CLICKCOST).cast(SQLDataType.INTEGER).as("cost"))
						.from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)).groupBy(field("divTime"))
						.unionAll(
							create.select(epoch(IMPR.DATE).div(getInterval()).as("divTime"), sum(IMPR.IMPRESSIONCOST).cast(SQLDataType.INTEGER).as("cost"))
								.from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).groupBy(field("divTime"))
						)
				).groupBy(field("divTime")).asTable();

			Table<Record2<Integer, Integer>> imprCounts = create
				.select(epoch(IMPR.DATE).div(getInterval()).as("divTime"), count(asterisk()).cast(SQLDataType.INTEGER).as("impressions"))
				.from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).groupBy(field("divTime")).asTable();

			Field<Integer> time = imprCounts.field("divTime").cast(SQLDataType.INTEGER).mul(getInterval());

			// Left join on impression count - this excludes values where there are no impressions, but those
			// would return infinite/invalid values anyway
			Result<Record2<Integer, Float>> result = create
				// When no value is available for cost, replace with 0
				.select(time, coalesce(costs.field("cost").cast(SQLDataType.REAL), 0F)
					.div(imprCounts.field("impressions").cast(SQLDataType.REAL).div(1000)))
				.from(imprCounts.leftJoin(costs).using(field("divTime")))
				.fetch();

			return getResults(result);
		} else {
			var totalClickCost = sum(CLICK.CLICKCOST).as("totalClickCost");
			var totalImpressionCost = sum(IMPR.IMPRESSIONCOST).as("totalImpressionCost");
			var totalCost = totalClickCost.plus(totalImpressionCost).cast(SQLDataType.REAL).as("totalCost");
			var impressionCount = count(asterisk()).div(1000).as("impressionCount");

			Result<Record1<Float>> result = create.select(totalCost.div(impressionCount).cast(SQLDataType.REAL)).from(
				create.select(totalCost).from(
					create.select(totalClickCost).from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)),
					create.select(totalImpressionCost).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR))
				),
				create.select(impressionCount).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR))
			).fetch();

			return getResults(result);
		}
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(copyTo(new CostPerThousandImpressionsQuery(dbm))::execute);
	}
}
