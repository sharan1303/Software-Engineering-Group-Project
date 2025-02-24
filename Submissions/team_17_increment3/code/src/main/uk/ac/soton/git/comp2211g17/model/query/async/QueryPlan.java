package uk.ac.soton.git.comp2211g17.model.query.async;

import javafx.beans.Observable;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;

public class QueryPlan {
	protected final QueryExecutor executor;
	protected final Observable[] dependencies;

	public QueryPlan(QueryExecutor executor, Observable... dependencies) {
		this.executor = executor;
		this.dependencies = dependencies;
	}

	// TODO: somehow enforce equals?

	/**
	 * Interface implemented by queries that carries out the query's execution. All data stored/captured in this object
	 * must be threadsafe and not modified after it is submitted in a query plan.
	 */
	@FunctionalInterface
	public interface QueryExecutor {
		Column<?>[] execute() throws SQLException;
	}
}
