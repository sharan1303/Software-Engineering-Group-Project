package uk.ac.soton.git.comp2211g17.model.query;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.model.files.DBFileManager;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class QueryTest {
    private Query query;

    @BeforeAll
    static void setUp() throws IOException {
        if (DatabaseManager.getInstance() == null) {
            DBFileManager.openFile(null);
            DatabaseManager.setupDatabaseManager(DBFileManager.getOpenFile());
            DatabaseManager.getInstance().connectToAuctionDB();
        }
    }

    @BeforeEach
    public abstract void setUpEach();

    public void setQuery(Query query) { this.query = query; }

    public Query getQuery() {
        return query;
    }

    @Test
    void getFieldsReturnsValues() {
        assertNotNull(query.getFields());
        assertTrue(query.getFields().length > 0);
    }

    @Test
    public void executeReturnsData() throws SQLException {
        Column<?>[] result = query.execute();
        assertEquals(1, result.length);
        assertEquals(1, result[0].getData().size());
    }

    @Test
    public void executeReturnsSameFieldsAsGetFields() throws SQLException {
        Field<?>[] fields = query.getFields();
        Column<?>[] result = query.execute();
        for (int i = 0; i < fields.length; i++) {
            assertEquals(result[i].getField(), fields[i]);
        }
    }
}
