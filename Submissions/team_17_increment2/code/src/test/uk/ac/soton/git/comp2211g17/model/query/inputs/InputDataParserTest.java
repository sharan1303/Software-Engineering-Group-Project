package uk.ac.soton.git.comp2211g17.model.query.inputs;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;
import uk.ac.soton.git.comp2211g17.model.files.DBFileManager;
import uk.ac.soton.git.comp2211g17.model.inputs.InputDataParser;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.jooq.Record;
import static org.jooq.impl.DSL.asterisk;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InputDataParserTest {
    static String CLICK_TEST_DATA = "Date,ID,Click Cost\n" +
            "2015-01-01 12:01:21,8895519749317550080,11.794442\n" +
            "2015-01-01 12:01:33,6487139546184780800,11.718663";
    static String IMPR_TEST_DATA = "Date,ID,Gender,Age,Income,Context,Impression Cost\n" +
            "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713\n" +
            "2015-01-01 12:00:04,3365479180556158976,Female,35-44,Medium,News,0.002762";
    static String SRV_TEST_DATA = "Entry Date,ID,Exit Date,Pages Viewed,Conversion\n" +
            "2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,No\n" +
            "2015-01-01 12:01:34,6487139546184780800,2015-01-01 12:02:01,1,No";

    @BeforeAll
    static void setUp() throws IOException {
        if (DatabaseManager.getInstance() == null) {
            DBFileManager.openFile(null);
            DatabaseManager.setupDatabaseManager(DBFileManager.getOpenFile());
            DatabaseManager.getInstance().connectToAuctionDB();
        }
    }

    @Test
    public void parsesClickLogData() {
        try {
            DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
            Result<Record> r = create.select(asterisk()).from(Click.CLICK).fetch();

            int size = r.size();

            InputDataParser.parse(DatabaseManager.getInstance(), new BufferedReader(new StringReader(CLICK_TEST_DATA)), "DATE,ID,CLICK-COST", true);

            r = create.select(asterisk()).from(Click.CLICK).fetch();

            assertEquals(size + 2, r.size());
        } catch (IOException e) {
            fail("An unexpected exception occurred while parsing.");
        }
    }

    @Test
    public void parsesImpressionLogData() {
        try {
            DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
            Result<Record> r = create.select(asterisk()).from(Impr.IMPR).fetch();

            int size = r.size();

            InputDataParser.parse(DatabaseManager.getInstance(), new BufferedReader(new StringReader(IMPR_TEST_DATA)), "DATE,ID,USER-GENDER,USER-AGE,INCOME,CONTEXT,IMPRESSION-COST", true);

            r = create.select(asterisk()).from(Impr.IMPR).fetch();

            assertEquals(size + 2, r.size());
        } catch (IOException e) {
            fail("An unexpected exception occurred while parsing.");
        }
    }

    @Test
    public void parsesServerLogData() {
        try {
            DSLContext create = DSL.using(DatabaseManager.getInstance().conn, SQLDialect.SQLITE);
            Result<Record> r = create.select(asterisk()).from(Srv.SRV).fetch();

            int size = r.size();

            InputDataParser.parse(DatabaseManager.getInstance(), new BufferedReader(new StringReader(SRV_TEST_DATA)), "DATE,ID,DATE-EXIT,VIEWED-PAGES,CONVERSION", true);

            r = create.select(asterisk()).from(Srv.SRV).fetch();

            assertEquals(size + 2, r.size());
        } catch (IOException e) {
            fail("An unexpected exception occurred while parsing.");
        }
    }
}
