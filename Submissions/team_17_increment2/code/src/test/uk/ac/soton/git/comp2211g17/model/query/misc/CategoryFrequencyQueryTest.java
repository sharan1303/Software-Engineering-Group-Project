package uk.ac.soton.git.comp2211g17.model.query.misc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.QueryTest;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryFrequencyQueryTest extends QueryTest {
    @BeforeEach
    public void setUpEach() {
        setQuery(new CategoryFrequencyQuery(DatabaseManager.getInstance()));
    }

    @Test
    public void executeReturnsData() throws SQLException {
        Column<?>[] result = getQuery().execute();
        assertEquals(2, result.length);
    }
}
