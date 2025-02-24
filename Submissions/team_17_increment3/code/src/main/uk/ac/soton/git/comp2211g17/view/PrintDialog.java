package uk.ac.soton.git.comp2211g17.view;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.Subscription;
import org.fxmisc.easybind.monadic.MonadicObservableValue;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PrintDialog implements Initializable {
	public TextField titleField;

	private Stage stage;
	private Node nodeToPrint;
	private final ObjectProperty<PrinterJob> job = new SimpleObjectProperty<>();
	private final MonadicObservableValue<PageLayout> pageLayout = EasyBind.monadic(job)
		.map(PrinterJob::getJobSettings)
		.flatMap(JobSettings::pageLayoutProperty);

	private final StringProperty title = new SimpleStringProperty("Ad Auction Report - " + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
	private final StringProperty printerLabelText = Utils.propBound(new SimpleStringProperty(), EasyBind.monadic(job)
		.flatMap(PrinterJob::printerProperty).map(p -> "Printer: " + p.getName()).orElse("Printer: "));
	private final StringProperty pageSizeLabelText = Utils.propBound(new SimpleStringProperty(), pageLayout
		.map(layout -> "Page Size: " + layout.getPaper().getName()).orElse("Page Size: "));

	private static final PseudoClass PRINT_CLASS = PseudoClass.getPseudoClass("print");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleField.textProperty().bindBidirectional(title);
	}

	public static void openPrintDialog(Window application, BorderPane nodeToPrint) {
		Stage stage = new Stage();
		Pair<Parent, Object> rootPair = Utils.loadFXMLWithController("fxml/PrintDialog.fxml");
		Parent root = rootPair.getKey();

		stage.setTitle("Print");

		Scene scene = new Scene(root, 400, 270);
		stage.setScene(scene);
		stage.setMinWidth(400);
		stage.setMinHeight(270);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(application);

		PrintDialog dialog = (PrintDialog)rootPair.getValue();
		dialog.stage = stage;
		dialog.nodeToPrint = nodeToPrint;

		PrinterJob job = PrinterJob.createPrinterJob();
		dialog.job.set(job);
		if (job == null) {
			return;
		}

		nodeToPrint.pseudoClassStateChanged(PRINT_CLASS, true);

		Subscription layoutSub = EasyBind.subscribe(dialog.pageLayout, layout -> {
			Bounds pageBounds = nodeToPrint.getLayoutBounds();
			double width = layout.getPrintableWidth();
			double scaleFactor = width / pageBounds.getWidth();
			nodeToPrint.setTranslateX((pageBounds.getWidth() * scaleFactor - pageBounds.getWidth()) / 2);
			nodeToPrint.setTranslateY((pageBounds.getHeight() * scaleFactor - pageBounds.getHeight()) / 2);
			nodeToPrint.setScaleX(scaleFactor);
			nodeToPrint.setScaleY(scaleFactor);
		});

		Label title = new Label();
		title.textProperty().bind(dialog.title);
		title.getStyleClass().addAll("title");
		BorderPane.setAlignment(title, Pos.CENTER);
		nodeToPrint.setTop(title);

		stage.showAndWait();

		job.endJob();

		nodeToPrint.setTop(null);
		layoutSub.unsubscribe();
		nodeToPrint.setTranslateX(0);
		nodeToPrint.setTranslateY(0);
		nodeToPrint.setScaleX(1);
		nodeToPrint.setScaleY(1);

		nodeToPrint.pseudoClassStateChanged(PRINT_CLASS, false);
	}

	public PrinterJob getJob() {
		return job.get();
	}

	public Property<PrinterJob> jobProperty() {
		return job;
	}

	public void setJob(PrinterJob job) {
		this.job.set(job);
	}

	public void print() {
		job.getValue().printPage(nodeToPrint);
		job.getValue().endJob();
		close();
	}

	public void close() {
		stage.close();
	}

	public void configureButton() {
		job.getValue().showPrintDialog(stage);
	}

	public void pageSetupButton() {
		job.getValue().showPageSetupDialog(stage);
	}

	public String getPrinterLabelText() {
		return printerLabelText.get();
	}

	public StringProperty printerLabelTextProperty() {
		return printerLabelText;
	}

	public void setPrinterLabelText(String printerLabelText) {
		this.printerLabelText.set(printerLabelText);
	}

	public String getPageSizeLabelText() {
		return pageSizeLabelText.get();
	}

	public StringProperty pageSizeLabelTextProperty() {
		return pageSizeLabelText;
	}

	public void setPageSizeLabelText(String pageSizeLabelText) {
		this.pageSizeLabelText.set(pageSizeLabelText);
	}
}
