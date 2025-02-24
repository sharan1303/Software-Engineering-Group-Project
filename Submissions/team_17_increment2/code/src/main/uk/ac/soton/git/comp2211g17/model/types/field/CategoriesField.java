package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Category;
import uk.ac.soton.git.comp2211g17.model.types.Field;

/**
 * Field for a column that provides data as {@link Category} instances, from a fixed set of categories
 *
 * @param <T> The category type of this field
 */
public class CategoriesField<T extends Category> extends Field<T> {
	private final T[] categories;

	public CategoriesField(String name, T[] categories) {
		super(name);
		this.categories = categories;
	}

	/**
	 * @return The possible categories this field can contain
	 */
	public T[] getCategories() {
		return this.categories;
	}

	@Override
	public String format(T value) {
		return value.getName();
	}
}
