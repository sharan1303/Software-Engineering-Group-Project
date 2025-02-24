package uk.ac.soton.git.comp2211g17.controller;

import javafx.beans.property.ReadOnlyProperty;
import uk.ac.soton.git.comp2211g17.model.types.Column;

public interface ChartController<T extends Column<?>, U extends Column<?>> {
	ReadOnlyProperty<ChartData<T, U>> getChartDataProperty();

	class ChartData<T extends Column<?>, U extends Column<?>> {
		public final T horizontalAxis;
		public final U verticalAxis;

		public ChartData(T horizontalAxis, U verticalAxis) {
			this.horizontalAxis = horizontalAxis;
			this.verticalAxis = verticalAxis;
		}
	}
}
