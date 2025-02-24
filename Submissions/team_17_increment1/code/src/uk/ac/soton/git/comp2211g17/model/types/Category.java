package uk.ac.soton.git.comp2211g17.model.types;

import uk.ac.soton.git.comp2211g17.model.types.column.CategoriesColumn;
import uk.ac.soton.git.comp2211g17.model.types.field.CategoriesField;

/**
 * A value that is a discrete category (e.g. age range, context), and has an associated user-visible name
 * Instances of this interface should ideally be compared using {@link Object#equals(Object) }, not by their names.<br>
 * <br>
 * Commonly implemented as an enum, where each value has it's name passed in the constructor.<br>
 * Use {@link CategoriesColumn} to store a column with category-based data.<br>
 * Use {@link CategoriesField#getCategories()} to query the list of available categories for a field.
 */
public interface Category {
	String getName();
}
