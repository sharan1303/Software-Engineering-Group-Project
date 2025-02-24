package uk.ac.soton.git.comp2211g17.view.components.async;

import javafx.beans.DefaultProperty;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.fxmisc.easybind.EasyBind;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;

import java.util.Objects;

/**
 * Wraps a Node with a progress spinner and exception label attached to the given FutureIndicatorViewModel
 */
@DefaultProperty("wrappedNode")
public class FutureIndicatorContainer extends StackPane {
	private final ObjectProperty<Node> wrappedNode = new SimpleObjectProperty<>();
	private final ObjectProperty<FutureIndicatorViewModel> indicator = new SimpleObjectProperty<>();

	public FutureIndicatorContainer() {
		Binding<Boolean> processing = EasyBind.monadic(indicator).flatMap(FutureIndicatorViewModel::processingProperty).orElse(false);
		Binding<Boolean> hasException = EasyBind.monadic(indicator).flatMap(FutureIndicatorViewModel::exceptionProperty).map(Objects::nonNull).orElse(false);
		Binding<String> exception = EasyBind.monadic(indicator).flatMap(FutureIndicatorViewModel::exceptionProperty).map(Throwable::toString).orElse("");

		StackPane dimPane = new StackPane();
		dimPane.setBackground(new Background(new BackgroundFill(Color.BLACK.deriveColor(0, 0, 0, 0.2), null, null)));
		dimPane.setMouseTransparent(true);
		dimPane.visibleProperty().bind(BooleanBinding.booleanExpression(processing).or(BooleanExpression.booleanExpression(hasException)));

		ProgressIndicator processingIndicator = new ProgressIndicator();
		processingIndicator.setMouseTransparent(true);
		processingIndicator.visibleProperty().bind(processing);

		Label exceptionLabel = new Label();
		exceptionLabel.setMouseTransparent(true);
		exceptionLabel.textProperty().bind(exception);
		exceptionLabel.visibleProperty().bind(hasException);

		getChildren().addAll(dimPane, processingIndicator, exceptionLabel);

		wrappedNode.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				getChildren().remove(oldValue);
			}
			if (newValue != null) {
				getChildren().add(0, newValue);
			}
		});

		indicator.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.visibleProperty().unbind();
			}
			if (newValue != null) {
				newValue.visibleProperty().bind(EasyBind.map(sceneProperty(), Objects::nonNull));
			}
		});
	}

	public Node getWrappedNode() {
		return wrappedNode.get();
	}

	public ObjectProperty<Node> wrappedNodeProperty() {
		return wrappedNode;
	}

	public void setWrappedNode(Node wrappedNode) {
		this.wrappedNode.set(wrappedNode);
	}

	public FutureIndicatorViewModel getIndicator() {
		return indicator.get();
	}

	public ObjectProperty<FutureIndicatorViewModel> indicatorProperty() {
		return indicator;
	}

	public void setIndicator(FutureIndicatorViewModel indicator) {
		this.indicator.set(indicator);
	}
}
