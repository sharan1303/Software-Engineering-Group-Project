package uk.ac.soton.git.comp2211g17.model.query;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;
import uk.ac.soton.git.comp2211g17.model.files.DBFileManager;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;

import java.io.IOException;
import java.util.ArrayList;

import static org.jooq.impl.DSL.asterisk;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseManagerTest {
    @BeforeAll
    static void setUp() throws IOException {
        if (DatabaseManager.getInstance() == null) {
            DBFileManager.openFile(null);
            DatabaseManager.setupDatabaseManager(DBFileManager.getOpenFile());
            DatabaseManager.getInstance().connectToAuctionDB();
        }
    }

    @Test
    public void insertsIntoClickLog() {
        DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
        Result<Record> r = create.select(asterisk()).from(Click.CLICK).fetch();

        int size = r.size();

        ArrayList<DatabaseManager.ClickLogEntry> entries = new ArrayList<>();
        entries.add(new DatabaseManager.ClickLogEntry("2015-01-01 12:01:21",
                8895519749317550080L, 11.794442));

        DatabaseManager.getInstance().batchInsertClickLog(entries);

        r = create.select(asterisk()).from(Click.CLICK).fetch();

        assertEquals(size + 1, r.size());
    }

    @Test
    public void insertsIntoImpressionLog() {
        DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
        Result<Record> r = create.select(asterisk()).from(Impr.IMPR).fetch();

        int size = r.size();

        ArrayList<DatabaseManager.ImpressionLogEntry> entries = new ArrayList<>();
        entries.add(new DatabaseManager.ImpressionLogEntry("2015-01-01 12:01:21",
                8895519749317550080L, AudienceAge.YOUNG.toString(), Gender.MALE.toString(), IncomeGroup.LOW.toString(), Context.HOBBIES.toString(), 0.123));

        DatabaseManager.getInstance().batchInsertImpressionLog(entries);

        r = create.select(asterisk()).from(Impr.IMPR).fetch();

        assertEquals(size + 1, r.size());
    }

    @Test
    public void insertsIntoServerLog() {
        DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
        Result<Record> r = create.select(asterisk()).from(Srv.SRV).fetch();

        int size = r.size();

        ArrayList<DatabaseManager.ServerLogEntry> entries = new ArrayList<>();
        entries.add(new DatabaseManager.ServerLogEntry("2015-01-01 12:01:21",
                8895519749317550080L, "2015-01-01 12:02:22",2,false));

        DatabaseManager.getInstance().batchInsertServerLog(entries);

        r = create.select(asterisk()).from(Srv.SRV).fetch();

        assertEquals(size + 1, r.size());
    }
}
