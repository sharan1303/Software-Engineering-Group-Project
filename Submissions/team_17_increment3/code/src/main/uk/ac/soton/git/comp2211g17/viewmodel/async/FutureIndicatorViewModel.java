package uk.ac.soton.git.comp2211g17.viewmodel.async;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface FutureIndicatorViewModel {
	boolean isVisible();
	/**
	 * Update this property when the result is visible and should be prioritised in query scheduling
	 */
	BooleanProperty visibleProperty();
	void setVisible(boolean visible);

	boolean isProcessing();
	/**
	 * Read this property to know when the query is being processed and new data will be updated soon, so a progress spinner should be shown.
	 */
	ReadOnlyBooleanProperty processingProperty();

	Throwable getException();
	ReadOnlyObjectProperty<Throwable> exceptionProperty();
}
