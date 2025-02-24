package uk.ac.soton.git.comp2211g17.view.components;

import javafx.beans.DefaultProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import org.fxmisc.easybind.EasyBind;
import org.jetbrains.annotations.Nullable;

public class Navigation extends BorderPane {
	private final ObservableList<Page> pages = FXCollections.observableArrayList();
	private final Property<String> defaultPath = new SimpleStringProperty();

	private final Property<Page> currentPage = new SimpleObjectProperty<>();
	private final Property<String> currentTitle = new SimpleStringProperty();

	private final ObjectProperty<ScrollPane> scrollPane = new SimpleObjectProperty<>();

	public Navigation() {
		layout();

		pages.addListener((ListChangeListener<Page>) c -> {
			for (Page page : pages) {
				page.parent = this;
			}
			updateDefault();
		});

		currentPage.addListener((observable, oldValue, newValue) -> {
			setCenter(newValue.getContent());
		});

		currentTitle.bind(EasyBind.select(currentPage).selectObject(Page::titleProperty));
	}

	private void updateDefault() {
		if (pages.size() > 0 && defaultPath.getValue() != null && currentPage.getValue() == null) {
			navigate(defaultPath.getValue());
		}
	}

	public ObservableList<Page> getPages() {
		return pages;
	}

	public String getDefaultPath() {
		return defaultPath.getValue();
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath.setValue(defaultPath);
		updateDefault();
	}

	public Property<String> defaultPathProperty() {
		return defaultPath;
	}

	public String getCurrentTitle() {
		return currentTitle.getValue();
	}

	public ReadOnlyProperty<String> currentTitleProperty() {
		return currentTitle;
	}

	public void navigate(String path) {
		String pathNoMetadata = path;
		String metadata = null;

		int metaOffset = path.indexOf('#');
		if (metaOffset != -1) {
			pathNoMetadata = path.substring(0, metaOffset);
			metadata = path.substring(metaOffset + 1);
		}
		for (Page page : pages) {
			if (page.getPath().equals(pathNoMetadata)) {
				currentPage.setValue(page);
				page.metadata.setValue(metadata);
				// Scroll back to the origin when navigating
				if (scrollPane.get() != null) {
					scrollPane.get().setVvalue(0);
					scrollPane.get().setHvalue(0);
				}
				break;
			}
		}
	}

	@DefaultProperty("content")
	public static class Page {
		private Navigation parent;
		private final Property<Node> content = new SimpleObjectProperty<>();
		private final Property<String> path = new SimpleStringProperty();
		private final Property<String> metadata = new SimpleStringProperty();
		private final Property<String> title = new SimpleStringProperty();

		public Node getContent() {
			return content.getValue();
		}

		public void setContent(Node node) {
			content.setValue(node);
		}

		public Property<Node> contentProperty() {
			return content;
		}

		public String getPath() {
			return path.getValue();
		}

		public void setPath(String path) {
			this.path.setValue(path);
		}

		public Property<String> pathProperty() {
			return path;
		}

		public Navigation getParentNavigation() {
			return parent;
		}

		@Nullable
		public String getMetadata() {
			return metadata.getValue();
		}

		public void setMetadata(@Nullable String value) {
			metadata.setValue(value);
		}

		public Property<String> metadataProperty() {
			return metadata;
		}

		public String getTitle() {
			return title.getValue();
		}

		public void setTitle(String value) {
			title.setValue(value);
		}

		public Property<String> titleProperty() {
			return title;
		}
	}

	public ScrollPane getScrollPane() {
		return scrollPane.get();
	}

	public Property<ScrollPane> scrollPaneProperty() {
		return scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane.set(scrollPane);
	}
}
