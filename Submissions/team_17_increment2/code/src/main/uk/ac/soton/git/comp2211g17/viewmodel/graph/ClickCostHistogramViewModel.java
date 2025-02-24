package uk.ac.soton.git.comp2211g17.viewmodel.graph;

import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.misc.ClickCostQuery;

public class ClickCostHistogramViewModel extends HistogramViewModel {
	public ClickCostHistogramViewModel() {
		super(new ClickCostQuery(DatabaseManager.getInstance()));
	}
}
