package uk.ac.soton.git.comp2211g17.model.query.misc;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import static org.jooq.impl.DSL.count;
import static uk.ac.soton.git.comp2211g17.generated.Tables.USER;

/**
 * A query for category frequencies in impressions
 */
public class CategoryFrequencyQuery implements Query {
	public static final CategoriesField<Context> context = new CategoriesField<>("Context", Context.values());
	public static final IntegerField frequency = new IntegerField("Frequency");

	private final DatabaseManager dbm;

	public CategoryFrequencyQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{context, frequency};
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Result<Record2<Context, Integer>> result = create.select(USER.CONTEXT, count(USER.CONTEXT)).from(USER).groupBy(USER.CONTEXT).fetch();

		return new Column<?>[]{
			context.fromJooqField(result, USER.CONTEXT),
			frequency.fromJooqField(result, result.field(count(USER.CONTEXT)))
		};
	}
}
