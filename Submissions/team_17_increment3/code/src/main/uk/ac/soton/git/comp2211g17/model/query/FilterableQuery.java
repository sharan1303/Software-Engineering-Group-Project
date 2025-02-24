package uk.ac.soton.git.comp2211g17.model.query;

import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;

import java.util.function.Function;

public interface FilterableQuery extends Query {
	void setFilter(Function<Table<?>, Filter> filter);
}
