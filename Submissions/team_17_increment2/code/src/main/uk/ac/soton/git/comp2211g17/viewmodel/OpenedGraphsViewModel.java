package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OpenedGraphsViewModel {
	public final ObservableList<KeyMetrics> openedGraphs = FXCollections.observableArrayList();
	public final ObservableList<KeyMetrics> remainingGraphs = FXCollections.observableArrayList(KeyMetrics.values());

	public ObservableList<KeyMetrics> getOpenedGraphs() {
		return openedGraphs;
	}

	public ObservableList<KeyMetrics> getRemainingGraphs() {
		return remainingGraphs;
	}

	public OpenedGraphsViewModel() {
		openedGraphs.addListener((ListChangeListener<? super KeyMetrics>) c ->
			remainingGraphs.setAll(
				Arrays.stream(KeyMetrics.values())
					.filter(keyMetrics -> !openedGraphs.contains(keyMetrics))
					.collect(Collectors.toList())));
	}
}
