package uk.ac.soton.git.comp2211g17.model.query;

import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;

import java.sql.SQLException;

// TODO: write some utility classes/methods for making queries?
// TODO: change these to use lists instead of arrays?
public interface Query {
	Field<?>[] getFields();

	Column<?>[] execute() throws SQLException;
}
