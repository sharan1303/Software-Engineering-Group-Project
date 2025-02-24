package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.IMPR;

public class ImpressionCountQuery extends KeyMetricQueryBase {
	public static final IntegerField count = new IntegerField("Number of Impressions");

	private final DatabaseManager dbm;

	public ImpressionCountQuery(DatabaseManager dbm) {
		super(count);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);

		if (getInterval() > 0) {
			Field<Integer> divTime = epoch(IMPR.DATE).div(getInterval()).as("divTime");
			Field<Integer> count = count(asterisk()).as("count");
			Field<Integer> time = divTime.mul(getInterval());

			// We use field("count").cast(SQLDataType.INTEGER) here because otherwise you'd do the aggregation on the already aggregated results?!
			Result<Record2<Integer, Integer>> result = create.select(time, field("count").cast(SQLDataType.INTEGER)).from(
				create.select(divTime, count).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).groupBy(divTime)
			).fetch();

			return getResults(result);
		} else {
			Result<Record1<Integer>> result = create.select(
				count(asterisk())
			).from(applyUserJoin(IMPR)).where(getFilterCondition(IMPR)).fetch();

			return getResults(result);
		}
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(copyTo(new ImpressionCountQuery(dbm))::execute);
	}
}
