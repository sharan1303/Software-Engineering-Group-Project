package uk.ac.soton.git.comp2211g17.model.query;

import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;

import java.sql.SQLException;

public interface Query {
	Field<?>[] getFields();

	Column<?>[] execute() throws SQLException;

	QueryPlan buildQueryPlan();
}
