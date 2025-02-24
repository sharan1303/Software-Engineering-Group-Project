package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import java.util.function.Function;

import static org.jooq.impl.DSL.trueCondition;
import static uk.ac.soton.git.comp2211g17.generated.Tables.USER;

public abstract class KeyMetricQueryBase implements KeyMetricQuery {
	private Function<Table<?>, Filter> filter = null;
	private int interval = -1;

	private final Field<Integer> timeField = new IntegerField("Time");
	private final Field<?>[] fields;

	public KeyMetricQueryBase(Field<?>... fields) {
		this.fields = fields;
	}

	@Override
	public final uk.ac.soton.git.comp2211g17.model.types.Field<?>[] getFields() {
		if (interval > 0) {
			Field<?>[] newArr = new Field<?>[fields.length + 1];
			System.arraycopy(fields, 0, newArr, 1, fields.length);
			newArr[0] = timeField;
			return newArr;
		} else {
			return fields;
		}
	}

	@Override
	public void setFilter(Function<Table<?>, Filter> filter) {
		this.filter = filter;
	}

	/**
	 * Adds a join to the USER table if a filter is applied
	 */
	protected Table<Record> applyUserJoin(Table<Record> table) {
		if (filter != null) {
			return table.leftJoin(USER).using(USER.ID);
		}
		return table;
	}

	/**
	 * Returns the condition from the applied filter, or the true condition if there is none
	 */
	protected Condition getFilterCondition(Table<?> table) {
		if (filter != null) {
			return filter.apply(table).getCondition();
		}
		return trueCondition();
	}

	@Override
	public void setInterval(int interval) {
		this.interval = interval;
	}

	protected int getInterval() {
		return this.interval;
	}

	protected Column<?>[] getResults(Result<? extends Record> result) {
		Field<?>[] actualFields = getFields();
		org.jooq.Field<?>[] jooqFields = result.fields();
		if (result.fields().length != actualFields.length) {
			throw new IllegalArgumentException("Bad number of fields passed to getResults");
		}
		Column<?>[] results = new Column<?>[jooqFields.length];
		for (int i = 0; i < jooqFields.length; i++) {
			results[i] = makeJavaGenericsHappy(result, actualFields[i], jooqFields[i]);
		}
		return results;
	}

	/**
	 * Java generics loves to complain at you when using two different wildcards. This makes it shut up.
	 */
	@SuppressWarnings("unchecked")
	private <V> Column<V> makeJavaGenericsHappy(Result<? extends Record> result, Field<V> actualField, org.jooq.Field<?> jooqField) {
		return actualField.fromJooqField(result, (org.jooq.Field<V>) jooqField);
	}

}
