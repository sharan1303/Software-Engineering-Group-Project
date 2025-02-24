package uk.ac.soton.git.comp2211g17.model.query.metrics;

import org.junit.jupiter.api.BeforeEach;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetricQueryTest;

class CostPerAcquisitionQueryTest extends KeyMetricQueryTest {

	@BeforeEach
	public void setUpEach() {
		setQuery(new CostPerAcquisitionQuery(DatabaseManager.getInstance()));
	}

}