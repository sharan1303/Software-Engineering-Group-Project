package uk.ac.soton.git.comp2211g17.model.types.category;

import org.jetbrains.annotations.NotNull;
import uk.ac.soton.git.comp2211g17.model.types.Category;

public enum IncomeGroup implements Category {
	HIGH("High"),
	MEDIUM("Medium"),
	LOW("Low");

	final String name;

	IncomeGroup(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static IncomeGroup getFromSQLName(String name) {
		for (IncomeGroup incomeGroup : IncomeGroup.values()) {
			if (incomeGroup.name.equals(name)) {
				return incomeGroup;
			}
		}
		return null;
	}

	public static class Converter implements org.jooq.Converter<String, IncomeGroup> {
		@Override
		public IncomeGroup from(String databaseObject) {
			return IncomeGroup.getFromSQLName(databaseObject);
		}

		@Override
		public String to(IncomeGroup userObject) {
			return userObject.name;
		}

		@Override
		public @NotNull Class<String> fromType() {
			return String.class;
		}

		@Override
		public @NotNull Class<IncomeGroup> toType() {
			return IncomeGroup.class;
		}
	}
}
