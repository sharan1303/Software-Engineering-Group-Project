package uk.ac.soton.git.comp2211g17.model.types.category;

import org.jetbrains.annotations.NotNull;
import uk.ac.soton.git.comp2211g17.model.types.Category;

public enum AudienceAge implements Category {
	YOUNG("<25", "<25"),
	YOUNG_ADULT("25 - 34", "25-34"),
	ADULT("35 - 44", "35-44"),
	OLD_ADULT("45 - 54", "45-54"),
	OLD("> 54", ">54");

	private final String name;
	private final String sqlName;

	AudienceAge(String name, String sqlName) {
		this.name = name;
		this.sqlName = sqlName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public static AudienceAge getFromSQLName(String name) {
		for (AudienceAge age : values()) {
			if (age.sqlName.equals(name)) {
				return age;
			}
		}
		return null;
	}

	public static class Converter implements org.jooq.Converter<String, AudienceAge> {
		@Override
		public AudienceAge from(String databaseObject) {
			return AudienceAge.getFromSQLName(databaseObject);
		}

		@Override
		public String to(AudienceAge userObject) {
			return userObject.sqlName;
		}

		@Override
		public @NotNull Class<String> fromType() {
			return String.class;
		}

		@Override
		public @NotNull Class<AudienceAge> toType() {
			return AudienceAge.class;
		}
	}
}
