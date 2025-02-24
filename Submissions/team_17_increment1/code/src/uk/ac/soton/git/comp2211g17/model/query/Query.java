package uk.ac.soton.git.comp2211g17.model.query;

import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;

public interface Query {
	Column<?>[] execute() throws SQLException;
}
