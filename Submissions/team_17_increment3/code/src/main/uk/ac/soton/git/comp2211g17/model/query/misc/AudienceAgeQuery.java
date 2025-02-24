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
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;
import uk.ac.soton.git.comp2211g17.model.types.field.LongField;

import static org.jooq.impl.DSL.count;
import static uk.ac.soton.git.comp2211g17.generated.Tables.USER;

public class AudienceAgeQuery implements Query {
	public static final CategoriesField<AudienceAge> audienceAge = new CategoriesField<>("Audience Age", AudienceAge.values());
	public static final LongField frequency = new LongField("Frequency");

	private final DatabaseManager dbm;

	public AudienceAgeQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{audienceAge, frequency};
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Result<Record2<AudienceAge, Long>> result = create.select(USER.AGE, count(USER.AGE).cast(SQLDataType.BIGINT)).from(USER).groupBy(USER.AGE).fetch();

		// TODO: make a util class that just does this for you
		return new Column<?>[]{
			audienceAge.fromJooqField(result, result.field(USER.AGE)),
			frequency.fromJooqField(result, result.field(count(USER.AGE).cast(SQLDataType.BIGINT)))
		};
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(new AudienceAgeQuery(dbm)::execute);
	}
}
