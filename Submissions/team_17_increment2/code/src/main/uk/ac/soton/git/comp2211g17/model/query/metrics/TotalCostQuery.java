package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.CurrencyField;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;
import static uk.ac.soton.git.comp2211g17.generated.Tables.IMPR;

public class TotalCostQuery extends KeyMetricQueryBase {
	public static final CurrencyField count = new CurrencyField("Total Cost");

	private final DatabaseManager dbm;

	public TotalCostQuery(DatabaseManager dbm) {
		super(count);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);

		if (getInterval() > 0) {
			Table<Record2<Integer, Integer>> costs =
				create.select(epoch(CLICK.DATE).div(getInterval()).as("divTime"), sum(CLICK.CLICKCOST).cast(SQLDataType.INTEGER).as("cost"))
					.from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)).groupBy(field("divTime"))
			.unionAll(
				create.select(epoch(IMPR.DATE).div(getInterval()).as("divTime"), sum(IMPR.IMPRESSIONCOST).cast(SQLDataType.INTEGER).as("cost"))
					.from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).groupBy(field("divTime"))
			).asTable();

			Field<Integer> time = costs.field("divTime").cast(SQLDataType.INTEGER).mul(getInterval());

			Result<Record2<Integer, Integer>> result = create
				.select(time, sum(costs.field("cost").cast(SQLDataType.INTEGER)).cast(SQLDataType.INTEGER))
				.from(costs).groupBy(costs.field("divTime")).fetch();

			return getResults(result);
		} else {
			var a = sum(CLICK.CLICKCOST).as("a");
			var b = sum(IMPR.IMPRESSIONCOST).as("b");
			Result<Record1<Float>> result = create.select(
				a.plus(b).cast(SQLDataType.REAL)
			).from(
				create.select(a).from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)),
				create.select(b).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR))
			).fetch();

			return getResults(result);
		}

	}
}
