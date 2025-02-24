package uk.ac.soton.git.comp2211g17.model.query;

/* Database Notes/Types

 *   CLick log - (date DATETIME, id BIGINT PRIMARY KEY, clickCost DOUBLE)
 *
 *   Impression Log - ( date DATETIME, id BIGINT PRIMARY KEY, age TEXT, gender TEXT,
 *       income TEXT, context TEXT, impressionCost DOUBLE)
 *
 *   Server Log - (entryDate DATETIME, id BIGINT PRIMARY KEY, exitDate DATETIME,
 *       pagesViewed SMALLINT, conversion BOOLEAN)
 *
 * */


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.ac.soton.git.comp2211g17.model.files.DBFile;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.List;

import static uk.ac.soton.git.comp2211g17.generated.Tables.META;
import static uk.ac.soton.git.comp2211g17.generated.Tables.SRV;

/**
 * DatabaseManager - class to maintain the DB connection and process queries
 *
 * @author Christos Protopapas, Konrad Sobczak
 * @version 1.0
 * @since 1.0
 */
public class DatabaseManager {
    private static DatabaseManager INSTANCE;

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public static void setupDatabaseManager(DBFile dbFile) throws IOException {
        if (INSTANCE != null) {
            throw new RuntimeException("Database manager has already been set up!");
        }
        INSTANCE = new DatabaseManager(dbFile);
    }

    private static final String templateDB = "template.db";
    public BooleanProperty reload = new SimpleBooleanProperty(); // TODO: clean up
    public Connection conn;
    protected String dbPath;
    private boolean freshDB = false;

    public Condition bounceCondition = SRV.PAGESVIEWED.le((short) 1);

    private static final int CURRENT_SCHEMA_VERSION = 2;


    public DatabaseManager(DBFile dbFile) {
        if (dbFile.getFile() == null) {
            this.freshDB = true;

            try {
                dbFile.copyToTemp(DatabaseManager.class.getClassLoader().getResourceAsStream(templateDB));
                System.out.println(dbFile.getWorkingFile().length());
            } catch (IOException e) {
                Utils.openErrorDialog("Disk operation failed", "Copy operation failed", e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        }

        this.dbPath = dbFile.getWorkingFile().getPath().toString();
        conn = connectToAuctionDB();
    }

    /** Class Constructor - Creates a database, establishes a single shared connection
     * with it to be shared in the class and sets up the 3 tables for use
     *
     * @param dbPath path to the DB file
     */
    public DatabaseManager(String dbPath) {
        Path targetDB = Paths.get(dbPath);

        if (!Files.exists(targetDB)) {
            InputStream templateDB = DatabaseManager.class.getClassLoader().getResourceAsStream("template.db");
            if (templateDB != null) {
                try {
                    Files.copy(templateDB, targetDB, StandardCopyOption.REPLACE_EXISTING);
                    freshDB = true;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            } else {
                Utils.openErrorDialog("Fatal error", "Accessing the template DB failed", "The template DB has been removed, check your installation");
                //System.out.println("The template DB has been removed, check your installation");
                System.exit(1);
            }
        }

        this.dbPath = dbPath;
        conn = connectToAuctionDB();
    }

    /** Class Constructor - Creates a database, establishes a single shared connection
     * with it to be shared in the class and sets up the 3 tables for use
     * with the db in the current working folder
     */
    public DatabaseManager(){
        this("ad_auction.db");
    }

    /**
     * Establishes connection to the database
     *
     * @return connection object
     */
    public Connection connectToAuctionDB(){
        String url = "jdbc:sqlite:" + this.dbPath;
        Connection conn = go(url);

        // Check the database schema version
        long version = 1;
        try {
            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
            Record1<Long> versionRecord = create.select(META.SCHEMAVERSION).from(META).fetchOne();
            if (versionRecord != null) {
                version = versionRecord.component1();
            }
        } catch (Exception ignored) {} // Ignore exceptions
        if (version < CURRENT_SCHEMA_VERSION) {
            Utils.openErrorDialog("Database schema outdated", "Database schema outdated", "Please delete and re-import the database");
        } else if (version > CURRENT_SCHEMA_VERSION) {
            Utils.openErrorDialog("Database schema too new", "Database schema too new", "Please update the application to open this database");
        }
        return conn;
    }

    public boolean databaseExists(){
        return !freshDB;
    }

    /**
     * Go to the url/directory to find and connect to the database
     * @param url target url
     *
     * @return Connection object set up to existing or created DB directory
     */
    public Connection go(String url){
        Connection conn;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                return conn;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void setPath(String path) {
        this.dbPath = path;
        this.conn = connectToAuctionDB();
    }

    public interface Entry {
        void prepareStatement(PreparedStatement ps) throws SQLException;
    }

    public static class ClickLogEntry implements Entry {
        String date;
        long ID;
        double clickCost;

        public ClickLogEntry(String date, long ID, double clickCost) {
            this.date = date;
            this.ID = ID;
            this.clickCost = clickCost;
        }

        @Override
        public void prepareStatement(PreparedStatement ps) throws SQLException {
            ps.setString(1, date);
            ps.setLong(2, ID);
            ps.setDouble(3, clickCost);
        }
    }

    public void reload() {
        this.reload.setValue(true);
        this.reload.setValue(false);
    }

    public void insertClickLog(String date, long id, double clickcost) {
        String sql = "INSERT INTO click(date,id,clickCost) VALUES (datetime(?),?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setLong(2, id);
            ps.setDouble(3, clickcost);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void batchInsertClickLog(List<ClickLogEntry> entries) {
        batchInsert(entries, "INSERT INTO click(date,id,clickCost) VALUES (datetime(?),?,?)");
    }

    private void batchInsert(List<? extends Entry> entries, String query) {
        try {
            conn.prepareStatement("BEGIN TRANSACTION").execute();
            PreparedStatement ps = conn.prepareStatement(query);

            for (Entry entry : entries) {
                entry.prepareStatement(ps);

                ps.addBatch();
            }

            ps.executeBatch();
            conn.prepareStatement("END TRANSACTION").execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertImpressionLog(String date, long id, String age, String gender, String income, String context, double impressionCost) {
        String sql = "INSERT INTO impr(date,id,impressionCost) VALUES(datetime(?),?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setLong(2, id);
            ps.setDouble(3, impressionCost);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = "INSERT INTO user(id,age,gender,income,context) VALUES(?,?,?,?,?) ON CONFLICT DO NOTHING;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql2);
            ps.setLong(1, id);
            ps.setString(2, age);
            ps.setString(3, gender);
            ps.setString(4, income);
            ps.setString(5, context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class ImpressionLogEntry implements Entry {
        String date;
        long id;
        String age;
        String gender;
        String income;
        String context;
        double impressionCost;

        public ImpressionLogEntry(String date, long id, String age, String gender, String income, String context, double impressionCost) {
            this.date = date;
            this.id = id;
            this.age = age;
            this.gender = gender;
            this.income = income;
            this.context = context;
            this.impressionCost = impressionCost;
        }

        // TODO: remove and clean up
        @Override
        public void prepareStatement(PreparedStatement ps) throws SQLException {
            ps.setString(1, date);
            ps.setLong(2, id);
            ps.setString(3, age);
            ps.setString(4, gender);
            ps.setString(5, income);
            ps.setString(6, context);
            ps.setDouble(7, impressionCost);
        }
    }

    public void batchInsertImpressionLog(List<ImpressionLogEntry> entries) {
        try {
            conn.prepareStatement("BEGIN TRANSACTION").execute();

            {
                String sql = "INSERT INTO impr(date,id,impressionCost) VALUES(datetime(?),?,?);";
                PreparedStatement ps = conn.prepareStatement(sql);
                for (ImpressionLogEntry entry : entries) {
                    ps.setString(1, entry.date);
                    ps.setLong(2, entry.id);
                    ps.setDouble(3, entry.impressionCost);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            {
                String sql = "INSERT INTO user(id,age,gender,income,context) VALUES(?,?,?,?,?) ON CONFLICT DO NOTHING;";
                PreparedStatement ps = conn.prepareStatement(sql);
                for (ImpressionLogEntry entry : entries) {
                    ps.setLong(1, entry.id);
                    ps.setString(2, entry.age);
                    ps.setString(3, entry.gender);
                    ps.setString(4, entry.income);
                    ps.setString(5, entry.context);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.prepareStatement("END TRANSACTION").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertServerLog(String entryDate, long id, String exitDate, int pagesviewed, boolean conversion) {
        String sql = "INSERT INTO srv(entryDate,id,exitDate,pagesViewed,conversion) VALUES (datetime(?),?,datetime(?),?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entryDate);
            ps.setLong(2, id);
            ps.setString(3, exitDate);
            ps.setInt(4, pagesviewed);
            ps.setBoolean(5, conversion);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static class ServerLogEntry implements Entry {
        String entryDate;
        long id;
        String exitDate;
        int pagesviewed;
        boolean conversion;

        public ServerLogEntry(String entryDate, long id, String exitDate, int pagesviewed, boolean conversion) {
            this.entryDate = entryDate;
            this.id = id;
            this.exitDate = exitDate;
            this.pagesviewed = pagesviewed;
            this.conversion = conversion;
        }

        @Override
        public void prepareStatement(PreparedStatement ps) throws SQLException {
            ps.setString(1, entryDate);
            ps.setLong(2, id);
            ps.setString(3, exitDate);
            ps.setInt(4, pagesviewed);
            ps.setBoolean(5, conversion);
        }
    }

    public void batchInsertServerLog(List<ServerLogEntry> entries) {
        batchInsert(entries, "INSERT INTO srv(entryDate,id,exitDate,pagesViewed,conversion) VALUES (datetime(?),?,datetime(?),?,?);");
    }
}
