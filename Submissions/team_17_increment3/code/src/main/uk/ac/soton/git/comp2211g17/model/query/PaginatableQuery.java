package uk.ac.soton.git.comp2211g17.model.query;

import java.sql.SQLException;

public interface PaginatableQuery extends Query {
	int getLength() throws SQLException;

	void setLimit(int limit);

	void setOffset(int offset);
}
