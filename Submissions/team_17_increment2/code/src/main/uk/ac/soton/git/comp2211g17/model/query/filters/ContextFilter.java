package uk.ac.soton.git.comp2211g17.model.query.filters;

import uk.ac.soton.git.comp2211g17.generated.tables.User;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;

public class ContextFilter extends Filter {
    public ContextFilter(Context context) {
        condition = User.USER.CONTEXT.eq(context);
    }

    public ContextFilter addContext(Context context) {
        condition = condition.or(User.USER.CONTEXT.eq(context));

        return this;
    }
}
