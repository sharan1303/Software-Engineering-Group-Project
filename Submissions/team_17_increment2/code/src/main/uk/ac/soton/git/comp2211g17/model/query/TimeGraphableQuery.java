package uk.ac.soton.git.comp2211g17.model.query;

public interface TimeGraphableQuery extends Query {
	void setInterval(int interval);

	default void removeInterval() {
		setInterval(-1);
	}
}
