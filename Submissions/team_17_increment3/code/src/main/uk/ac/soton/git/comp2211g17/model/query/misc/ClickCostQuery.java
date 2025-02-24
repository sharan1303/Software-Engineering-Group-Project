package uk.ac.soton.git.comp2211g17.model.query.misc;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.floor;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;

public class ClickCostQuery implements Query {
	public static final IntegerField clickCost = new IntegerField("Click Costs (p)");
	public static final IntegerField frequency = new IntegerField("Frequency");

	private final DatabaseManager dbm;

	public ClickCostQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{clickCost, frequency};
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Result<Record2<Integer, Integer>> result = create.select(
			floor(CLICK.CLICKCOST).cast(SQLDataType.INTEGER),
			count(floor(CLICK.CLICKCOST))
		).from(CLICK)
			.groupBy(floor(CLICK.CLICKCOST).cast(SQLDataType.INTEGER))
			.orderBy(floor(CLICK.CLICKCOST).cast(SQLDataType.INTEGER)).fetch();

		// TODO: make a util class that just does this for you
		return new Column<?>[]{
			// TODO: get rid of the silly result.field on the other queries ?!?!
			clickCost.fromJooqField(result, floor(CLICK.CLICKCOST).cast(SQLDataType.INTEGER)),
			frequency.fromJooqField(result, count(floor(CLICK.CLICKCOST)))
		};
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(new ClickCostQuery(dbm)::execute);
	}
}
