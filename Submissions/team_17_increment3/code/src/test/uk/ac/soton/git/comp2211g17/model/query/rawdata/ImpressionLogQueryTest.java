package uk.ac.soton.git.comp2211g17.model.query.rawdata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.QueryTest;
import uk.ac.soton.git.comp2211g17.model.query.rawdata.ImpressionLogQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImpressionLogQueryTest extends QueryTest {
    @BeforeEach
    public void setUpEach() {
        setQuery(new ImpressionLogQuery(DatabaseManager.getInstance()));
    }

    @Test
    public void executeReturnsData() throws SQLException {
        Column<?>[] result = getQuery().execute();
        assertEquals(7, result.length);
    }
}
