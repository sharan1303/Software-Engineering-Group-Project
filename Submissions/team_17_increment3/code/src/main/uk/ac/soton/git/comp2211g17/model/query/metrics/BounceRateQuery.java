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
import static uk.ac.soton.git.comp2211g17.generated.Tables.SRV;

public class BounceRateQuery extends KeyMetricQueryBase {
	public static final PercentageField bounceRate = new PercentageField("Bounce Rate");

	private final DatabaseManager dbm;

	public BounceRateQuery(DatabaseManager dbm) {
		super(bounceRate);
		this.dbm = dbm;
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Condition bounceCondition = dbm.bounceCondition.get();

		if (getInterval() > 0) {
			Table<Record2<Integer, Integer>> bounceCounts = create
				.select(epoch(SRV.ENTRYDATE).div(getInterval()).as("divTime"), count(asterisk()).cast(SQLDataType.INTEGER).as("bounces"))
				.from(applyUserJoin(SRV)).where(bounceCondition.and(getFilterCondition(SRV))).groupBy(field("divTime")).asTable();

			Table<Record2<Integer, Integer>> clickCounts = create
				.select(epoch(CLICK.DATE).div(getInterval()).as("divTime"), count(asterisk()).cast(SQLDataType.INTEGER).as("clicks"))
				.from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK)).groupBy(field("divTime")).asTable();

			Field<Integer> time = clickCounts.field("divTime").cast(SQLDataType.INTEGER).mul(getInterval());

			// Left join on click count - this excludes values where there are no clicks, but those
			// would return infinite/invalid values anyway
			Result<Record2<Integer, Float>> result = create
				// When no value is available for bounces, replace with 0
				.select(time, coalesce(bounceCounts.field("bounces").cast(SQLDataType.REAL), 0F)
					.div(clickCounts.field("clicks").cast(SQLDataType.REAL)))
				.from(clickCounts.leftJoin(bounceCounts).using(field("divTime")))
				.fetch();

			return getResults(result);
		} else {
			var a = count(asterisk()).cast(SQLDataType.REAL).as("a");
			var b = count(asterisk()).cast(SQLDataType.REAL).as("b");
			Result<Record1<Float>> result = create.select(
				a.divide(b)
			).from(
				create.select(a).from(applyUserJoin(SRV)).where(bounceCondition.and(getFilterCondition(SRV))),
				create.select(b).from(applyUserJoin(CLICK)).where(getFilterCondition(CLICK))
			).fetch();

			return getResults(result);
		}
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(copyTo(new BounceRateQuery(dbm))::execute, dbm.bounceCondition);
	}
}
