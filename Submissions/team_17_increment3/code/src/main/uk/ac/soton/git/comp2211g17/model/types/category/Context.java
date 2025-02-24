package uk.ac.soton.git.comp2211g17.model.types.category;

import org.jetbrains.annotations.NotNull;
import uk.ac.soton.git.comp2211g17.model.types.Category;

public enum Context implements Category {
	NEWS("News"),
	SHOPPING("Shopping"),
	SOCIAL_MEDIA("Social Media"),
	BLOG("Blog"),
	HOBBIES("Hobbies"),
	TRAVEL("Travel");

	final String name;

	Context(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static Context getFromSQLName(String name) {
		for (Context context : Context.values()) {
			if (context.name.equals(name)) {
				return context;
			}
		}
		return null;
	}

	public static class Converter implements org.jooq.Converter<String, Context> {
		@Override
		public Context from(String databaseObject) {
			return Context.getFromSQLName(databaseObject);
		}

		@Override
		public String to(Context userObject) {
			return userObject.name;
		}

		@Override
		public @NotNull Class<String> fromType() {
			return String.class;
		}

		@Override
		public @NotNull Class<Context> toType() {
			return Context.class;
		}
	}
}
