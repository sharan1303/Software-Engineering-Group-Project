package uk.ac.soton.git.comp2211g17.model.types.category;

import uk.ac.soton.git.comp2211g17.model.types.Category;

public enum AudienceAge implements Category {
	YOUNG("<25"),
	YOUNG_ADULT("25 - 34"),
	ADULT("35 - 44"),
	OLD_ADULT("45 - 54"),
	OLD("> 54");

	final String name;

	AudienceAge(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public static AudienceAge getFromSQLName(String name) {
		switch (name) {
			case "<25":
				return YOUNG;
			case "25-34":
				return YOUNG_ADULT;
			case "35-44":
				return ADULT;
			case "45-54":
				return OLD_ADULT;
			case ">54":
				return OLD;
		}
		return null;
	}
}
