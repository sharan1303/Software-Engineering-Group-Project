package uk.ac.soton.git.comp2211g17.model.query.impl;


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


import java.sql.*;
import java.util.List;

public class DatabaseManager {

    protected Connection conn;

    //Main method - proof of concept/ tests - currently works
    public static void main(String[] args) {
        DatabaseManager dbm = new DatabaseManager();
        dbm.connectToAuctionDB();
        dbm.setUpClickLog();
        dbm.setUpImpressionLog();
        dbm.setUpServerLog();
        dbm.insertServerLog("2020-08-02 12:30:11",1234567,"2020-08-02 11:11:11",3,false);
        dbm.insertClickLog("2020-08-02 12:30:11",151515115,0.2526);

    }

    /* Class Constructor - Creates a database, establishes a single shared connection
    *  with it to be shared in the class and sets up the 3 tables for use
    */
    public DatabaseManager(){
        conn = connectToAuctionDB();
        setUpClickLog();
        setUpImpressionLog();
        setUpServerLog();
    }

    //Establishes connection to the database
    public Connection connectToAuctionDB(){
        String url = "jdbc:sqlite:ad_auction.db";
        return (go(url));
    }

    //The below 3 functions create the tables in our database
    public void setUpClickLog(){
        String sql = "CREATE TABLE IF NOT EXISTS click(\n"
            + "     date DATETIME,\n"
            + "     id BIGINT,\n"
            + "     clickCost DOUBLE);";

        try (Statement sm = conn.createStatement()) {
            sm.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUpImpressionLog(){
        String sql = "CREATE TABLE IF NOT EXISTS impr(\n"
            + "     date DATETIME,\n"
            + "     id BIGINT,\n"
            + "     age TEXT,\n"
            + "     gender TEXT,\n"
            + "     income TEXT,\n"
            + "     context TEXT,\n"
            + "     impressionCost DOUBLE);";

        try (Statement sm = conn.createStatement()) {
            sm.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUpServerLog(){
        String sql = "CREATE TABLE IF NOT EXISTS srv(\n"
            + "     entryDate DATETIME,\n"
            + "     id BIGINT,\n"
            + "     exitDate DATETIME,\n"
            + "     pagesViewed SMALLINT,\n"
            + "     conversion BOOLEAN);";

        try (Statement sm = conn.createStatement()) {
            sm.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Gets the url/directory to find and connect to the database
    //Connects to it if it exists, or creates it otherwise
    public Connection go(String url){
        Connection conn = null;
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

//    TODO Make the below using "PreparedStatement" to ensure query use to be flexible and usable on multiple databases/tables
//    TODO Add more queries to cover more possibilities

    public void sortByDate(String tableName,String columnName,String descAsc){
        String sql;
        if (descAsc.equals("DESC"))
            sql = "SELECT * FROM ? ORDER BY ? DESC;";
        else
            sql = "SELECT * FROM ? ORDER BY ? ASC;";
        try {
            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1,tableName);
            p.setString(2,columnName);
            runQuery(p);
        } catch (SQLException e){ System.out.println(e.getMessage());}
    }

    //TODO work in progress - needs testing
    public void sortByContext(String tableName){
        String sql = "SELECT * FROM impr ORDER BY context;";
        runQueryOnImpr(sql);
    }

    public void sortByGender() {
        String sql = "SELECT * FROM impr ORDER BY gender;";
        runQueryOnImpr(sql);
    }

    public void sortById(String tableName) {
        try {
            String sql = "SELECT * FROM ? ORDER BY id;";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1, tableName);
            runQuery(p);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sortByIncome(){
        String sql = "SELECT * FROM impr ORDER BY income;";
        runQueryOnImpr(sql);
    }

    public void sortByImpressionCost(){
        String sql = "SELECT * FROM impr ORDER BY impressionCost ASC;";
        runQueryOnImpr(sql);
    }

    public ResultSet runQuery(PreparedStatement p) {
        ResultSet rs = null;
        try {
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }
        return rs;
    }

    // Receives a SQLite command as a string, establishes a connection with the database
    // Parses the string as a prepared statement in the connection and then executes it
    // and prints results
    public void runQueryOnImpr(String sql) {
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getDate("date") + "\t"
                    + rs.getInt("id") + "\t" + rs.getString("age")
                    + "\t" + rs.getString("income") + "\t" + rs.getString("context")
                    + "\t" + rs.getDouble("impressionCost"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
        String sql = "INSERT INTO impr(date,id,age,gender,income,context,impressionCost) VALUES(datetime(?),?,?,?,?,?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setLong(2, id);
            ps.setString(3, age);
            ps.setString(4, gender);
            ps.setString(5, income);
            ps.setString(6, context);
            ps.setDouble(7, impressionCost);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        batchInsert(entries, "INSERT INTO impr(date,id,age,gender,income,context,impressionCost) VALUES(datetime(?),?,?,?,?,?,?);");
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
