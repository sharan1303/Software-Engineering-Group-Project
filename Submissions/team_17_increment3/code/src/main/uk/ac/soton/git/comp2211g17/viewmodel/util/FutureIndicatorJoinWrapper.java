package uk.ac.soton.git.comp2211g17.viewmodel.util;

import javafx.beans.property.*;
import org.fxmisc.easybind.EasyBind;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;

public class FutureIndicatorJoinWrapper implements FutureIndicatorViewModel {
	private final BooleanProperty visible = new SimpleBooleanProperty();
	private final BooleanProperty processing = new SimpleBooleanProperty();
	private final ObjectProperty<Throwable> exception = new SimpleObjectProperty<>();

	public FutureIndicatorJoinWrapper(FutureIndicatorViewModel left, FutureIndicatorViewModel right) {
		left.visibleProperty().bind(visible);
		right.visibleProperty().bind(visible);

		processing.bind(left.processingProperty().or(right.processingProperty()));
		exception.bind(EasyBind.monadic(left.exceptionProperty()).orElse(right.exceptionProperty()));
	}

	@Override
	public boolean isVisible() {
		return visible.get();
	}

	@Override
	public BooleanProperty visibleProperty() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible.set(visible);
	}

	@Override
	public boolean isProcessing() {
		return processing.get();
	}

	@Override
	public ReadOnlyBooleanProperty processingProperty() {
		return processing;
	}

	@Override
	public Throwable getException() {
		return exception.get();
	}

	@Override
	public ReadOnlyObjectProperty<Throwable> exceptionProperty() {
		return exception;
	}
}
