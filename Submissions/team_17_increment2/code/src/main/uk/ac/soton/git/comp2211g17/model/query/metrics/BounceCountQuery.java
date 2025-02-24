package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.SRV;

public class BounceCountQuery extends KeyMetricQueryBase {
	public static final IntegerField count = new IntegerField("Number of Bounces");

	private final DatabaseManager dbm;

	public BounceCountQuery(DatabaseManager dbm) {
		super(count);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Condition bounceCondition = dbm.bounceCondition;

		if (getInterval() > 0) {
			Field<Integer> divTime = epoch(SRV.ENTRYDATE).div(getInterval()).as("divTime");
			Field<Integer> count = count(asterisk()).as("count");
			Field<Integer> time = divTime.mul(getInterval());

			// We use field("count").cast(SQLDataType.INTEGER) here because otherwise you'd do the aggregation on the already aggregated results?!
			Result<Record2<Integer, Integer>> result = create.select(time, field("count").cast(SQLDataType.INTEGER)).from(
				create.select(divTime, count).from(applyUserJoin(SRV))
					.where(bounceCondition.and(getFilterCondition(SRV))).groupBy(divTime)
			).fetch();

			return getResults(result);
		} else {
			Result<Record1<Integer>> result = create.select(
				count(asterisk())
			).from(applyUserJoin(SRV)).where(bounceCondition.and(getFilterCondition(SRV))).fetch();

			return getResults(result);
		}
	}
}
