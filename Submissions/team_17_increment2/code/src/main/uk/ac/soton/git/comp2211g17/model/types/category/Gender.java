package uk.ac.soton.git.comp2211g17.model.types.category;

import org.jetbrains.annotations.NotNull;
import uk.ac.soton.git.comp2211g17.model.types.Category;

public enum Gender implements Category {
	MALE("Male"),
	FEMALE("Female"),
	OTHER("Other");

	final String name;

	Gender(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static Gender getFromSQLName(String name) {
		for (Gender gender : Gender.values()) {
			if (gender.name.equals(name)) {
				return gender;
			}
		}
		return null;
	}

	public static class Converter implements org.jooq.Converter<String, Gender> {
		@Override
		public Gender from(String databaseObject) {
			return Gender.getFromSQLName(databaseObject);
		}

		@Override
		public String to(Gender userObject) {
			return userObject.name;
		}

		@Override
		public @NotNull Class<String> fromType() {
			return String.class;
		}

		@Override
		public @NotNull Class<Gender> toType() {
			return Gender.class;
		}
	}
}
