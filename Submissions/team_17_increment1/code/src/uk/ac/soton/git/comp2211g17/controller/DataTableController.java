package uk.ac.soton.git.comp2211g17.controller;

import javafx.beans.property.*;
import uk.ac.soton.git.comp2211g17.model.query.PaginatableQuery;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DataTableController {
	private final Query query;

	// Properties read by the view
	private final ObjectProperty<List<Column<?>>> dataInternal = new SimpleObjectProperty<>();
	public final ReadOnlyProperty<List<Column<?>>> data = dataInternal;

	// Properties set by the view
	public final IntegerProperty currentPage = new SimpleIntegerProperty();
	public final IntegerProperty pageSize = new SimpleIntegerProperty();

	// TODO: sorting, filtering, reordering, hiding of columns

	public DataTableController(Query query, int initialPageSize) {
		this.query = query;
		this.pageSize.set(initialPageSize);

		currentPage.addListener((observable, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				setPage(pageSize.get(), newValue.intValue());
			}
		});
		pageSize.addListener((observable, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				setPage(newValue.intValue(), currentPage.get());
			}
		});

		setPage(initialPageSize, 0);
	}

	private void setPage(int pageSize, int currentPage) {
		if (query instanceof PaginatableQuery) {
			PaginatableQuery pagQuery = (PaginatableQuery) query;
			pagQuery.setOffset(currentPage * pageSize);
			pagQuery.setLimit(pageSize);
		}
		try {
			dataInternal.set(Arrays.asList(query.execute()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
