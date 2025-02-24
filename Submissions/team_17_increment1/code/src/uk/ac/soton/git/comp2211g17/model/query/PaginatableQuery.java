package uk.ac.soton.git.comp2211g17.model.query;

public interface PaginatableQuery {
	void setLimit(int limit);
	void setOffset(int offset);
}
