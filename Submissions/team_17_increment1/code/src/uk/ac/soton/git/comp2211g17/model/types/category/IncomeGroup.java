package uk.ac.soton.git.comp2211g17.model.types.category;

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
}
