package uk.ac.soton.git.comp2211g17.model.query.rawdata;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.PaginatableQuery;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;
import uk.ac.soton.git.comp2211g17.model.types.field.DateTimeField;
import uk.ac.soton.git.comp2211g17.model.types.field.FloatField;
import uk.ac.soton.git.comp2211g17.model.types.field.IDField;

import java.sql.SQLException;

import static org.jooq.impl.DSL.count;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;
import static uk.ac.soton.git.comp2211g17.generated.Tables.USER;

/**
 * A query for the raw Click Log data, with pagination support
 */
public class ClickLogQuery implements Query, PaginatableQuery {
	public static final DateTimeField date = new DateTimeField("Date");
	public static final IDField id = new IDField("ID");
	public static final CategoriesField<Gender> gender = new CategoriesField<>("Gender", Gender.values());
	public static final CategoriesField<AudienceAge> audienceAge = new CategoriesField<>("Age", AudienceAge.values());
	public static final CategoriesField<IncomeGroup> incomeGroup = new CategoriesField<>("Income", IncomeGroup.values());
	public static final CategoriesField<Context> context = new CategoriesField<>("Context", Context.values());
	public static final FloatField clickCost = new FloatField("Click Cost");

	private int limit = 100;
	private int offset = 0;
	private final DatabaseManager dbm;

	public ClickLogQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{date, id, gender, audienceAge, incomeGroup, context, clickCost};
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Result<Record> result = create.select().from(CLICK).join(USER)
			.using(USER.ID).limit(limit).offset(offset).fetch();

		return new Column<?>[]{
			date.fromJooqField(result, CLICK.DATE),
			id.fromJooqField(result, CLICK.ID),
			gender.fromJooqField(result, USER.GENDER),
			audienceAge.fromJooqField(result, USER.AGE),
			incomeGroup.fromJooqField(result, USER.INCOME),
			context.fromJooqField(result, USER.CONTEXT),
			clickCost.fromJooqField(result, CLICK.CLICKCOST)
		};
	}

	@Override
	public int getLength() throws SQLException {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Record1<Integer> res = create.select(count()).from(CLICK).fetchOne();
		if (res == null) {
			throw new SQLException("No length found (is table valid?)");
		}
		return res.get(count());
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
