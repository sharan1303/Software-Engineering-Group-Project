package uk.ac.soton.git.comp2211g17.model.query.rawdata;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.PaginatableQuery;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;
import uk.ac.soton.git.comp2211g17.model.types.field.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.jooq.impl.DSL.count;
import static uk.ac.soton.git.comp2211g17.generated.Tables.*;

/**
 * A query for the raw Server Log data, with pagination support
 */
public class ServerLogQuery implements Query, PaginatableQuery {
	public static final DateTimeField entryDate = new DateTimeField("Entry Date");
	public static final DateTimeField exitDate = new DateTimeField("Exit Date");
	public static final IDField id = new IDField("ID");
	public static final CategoriesField<Gender> gender = new CategoriesField<>("Gender", Gender.values());
	public static final CategoriesField<AudienceAge> audienceAge = new CategoriesField<>("Age", AudienceAge.values());
	public static final CategoriesField<IncomeGroup> incomeGroup = new CategoriesField<>("Income", IncomeGroup.values());
	public static final CategoriesField<Context> context = new CategoriesField<>("Context", Context.values());
	public static final IntegerField pagesViewed = new IntegerField("Pages Viewed");
	public static final BooleanField conversion = new BooleanField("Conversion");

	private int limit = 100;
	private int offset = 0;
	private final DatabaseManager dbm;

	public ServerLogQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{entryDate, exitDate, id, gender, audienceAge, incomeGroup, context, pagesViewed, conversion};
	}

	public Column<?>[] execute() {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Result<Record9<LocalDateTime, LocalDateTime, Long, Gender, AudienceAge, IncomeGroup, Context, Integer, Boolean>> result =
			create.select(SRV.ENTRYDATE, SRV.EXITDATE, SRV.ID, USER.GENDER, USER.AGE, USER.INCOME,
				USER.CONTEXT, SRV.PAGESVIEWED.cast(SQLDataType.INTEGER), SRV.CONVERSION)
			.from(SRV).join(USER)
			.using(USER.ID).limit(limit).offset(offset).fetch();

		return new Column<?>[]{
			entryDate.fromJooqField(result, SRV.ENTRYDATE),
			exitDate.fromJooqField(result, SRV.EXITDATE),
			id.fromJooqField(result, SRV.ID),
			gender.fromJooqField(result, USER.GENDER),
			audienceAge.fromJooqField(result, USER.AGE),
			incomeGroup.fromJooqField(result, USER.INCOME),
			context.fromJooqField(result, USER.CONTEXT),
			pagesViewed.fromJooqField(result, SRV.PAGESVIEWED.cast(SQLDataType.INTEGER)),
			conversion.fromJooqField(result, SRV.CONVERSION)
		};
	}

	@Override
	public int getLength() throws SQLException {
		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Record1<Integer> res = create.select(count()).from(IMPR).fetchOne();
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
