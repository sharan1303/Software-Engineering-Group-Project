package uk.ac.soton.git.comp2211g17.model.query.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.generated.tables.User;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextFilterTest {
    private ContextFilter filter;

    @BeforeEach
    public void setUpEach() {
        filter = new ContextFilter(Context.NEWS);
    }

    @Test
    public void createCorrectCondition() {
        assertEquals(User.USER.CONTEXT.eq(Context.NEWS), filter.getCondition());
    }

    @Test
    public void handleAddContext() {
        filter.addContext(Context.HOBBIES);
        assertEquals(User.USER.CONTEXT.eq(Context.NEWS).or(User.USER.CONTEXT.eq(Context.HOBBIES)), filter.getCondition());
    }
}
