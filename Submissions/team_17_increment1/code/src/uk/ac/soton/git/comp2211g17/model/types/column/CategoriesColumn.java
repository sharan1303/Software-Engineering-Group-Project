package uk.ac.soton.git.comp2211g17.model.types.column;

import uk.ac.soton.git.comp2211g17.model.types.Category;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;

/**
 * Column that provides data as {@link Category} instances, from a fixed set of categories
 *
 * @param <T> The category type of this column
 */
public class CategoriesColumn<T extends Category> extends Column<CategoriesField<T>> {
	private final T[] data;

	public CategoriesColumn(CategoriesField<T> field, T[] data) {
		super(field, data.length);
		this.data = data;
	}

	public T[] getDataAsCategories() {
		return data;
	}

	@Override
	public String[] getDataAsFormatted() {
		String[] formatted = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				formatted[i] = "None";
				continue;
			}
			formatted[i] = data[i].getName();
		}
		return formatted;
	}
}
