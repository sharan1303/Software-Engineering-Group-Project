package uk.ac.soton.git.comp2211g17.model.query.async;

import javafx.beans.property.*;
import org.fxmisc.easybind.EasyBind;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;

import java.util.function.Consumer;

public class AsyncQueryHandler implements FutureIndicatorViewModel {
	private final ObjectProperty<QueryPlan> queryPlan = new SimpleObjectProperty<>();
	private final ObjectProperty<AsyncQueryFuture> queryFuture = new SimpleObjectProperty<>();

	/**
	 * Update this property when the result is visible and should be prioritised in query scheduling
	 */
	public final BooleanProperty visible = new SimpleBooleanProperty(true);

	private final BooleanProperty processingInternal = new SimpleBooleanProperty(true);
	/**
	 * Read this property to know when the query is being processed and new data will be updated soon, so a progress spinner should be shown.
	 */
	public final ReadOnlyBooleanProperty processing = processingInternal;

	private final ObjectProperty<Column<?>[]> resultInternal = new SimpleObjectProperty<>();
	public final ReadOnlyObjectProperty<Column<?>[]> result = resultInternal;

	private final ObjectProperty<Throwable> exceptionInternal = new SimpleObjectProperty<>();
	public final ReadOnlyObjectProperty<Throwable> exception = exceptionInternal;

	public AsyncQueryHandler() {
		queryFuture.bind(EasyBind.monadic(queryPlan)
			.map(plan -> DatabaseManager.getInstance().scheduler.getBoundFuture(plan, this)));
		processingInternal.bind(EasyBind.monadic(queryFuture)
			.flatMap(fut -> fut.processing).orElse(true));
		resultInternal.bind(EasyBind.monadic(queryFuture).flatMap(fut -> fut.result));
		exceptionInternal.bind(EasyBind.monadic(queryFuture).flatMap(fut -> fut.exception));

		// Bind query future as appropriate
		queryFuture.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.unbind(this);
			}
			if (newValue != null) {
				newValue.bind(this);
			}
		});
	}

	public void setQuery(Query query) {
		queryPlan.set(query.buildQueryPlan());
	}

	public void await(Consumer<Column<?>[]> listener) {
		result.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				listener.accept(newValue);
			}
		});
	}

	public boolean isVisible() {
		return visible.get();
	}

	public BooleanProperty visibleProperty() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible.set(visible);
	}

	public boolean isProcessing() {
		return processing.get();
	}

	public ReadOnlyBooleanProperty processingProperty() {
		return processing;
	}

	public Column<?>[] getResult() {
		return result.get();
	}

	public ReadOnlyObjectProperty<Column<?>[]> resultProperty() {
		return result;
	}

	public Throwable getException() {
		return exception.get();
	}

	public ReadOnlyObjectProperty<Throwable> exceptionProperty() {
		return exception;
	}
}
