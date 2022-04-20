package red.fizz.roseapi;

import java.sql.*;

import static java.util.UUID.randomUUID;

public class MySQLConnection {

    // thanks to harrydev
    // for letting me
    // steal his "homework"

    static Connection connection;

    public static void openConnection(RoseAPI plugin) {
        final String username = plugin.getConfig().getString("mysql.username");
        final String password = plugin.getConfig().getString("mysql.password");
        final String url = "jdbc:mysql://" + plugin.getConfig().getString("mysql.host") + "/" + plugin.getConfig().getString("mysql.database");

        try {
            plugin.getLogger().info("Attempting to connect to the MySQL database...");
            connection = DriverManager.getConnection(url, username, password);
            plugin.getLogger().info("Successfully connected to the MySQL database.");
            createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(RoseAPI plugin) {
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
                plugin.getLogger().info("Successfully closed the MySQL connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void createDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS apikeys (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, playerUUID CHAR(36), apikey CHAR(36));";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean newKey(String playerUUID, String key) {
        if (key == null) {
            key = randomUUID().toString();
        }
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM apikeys WHERE playerUUID=?;");
            deleteStatement.setString(1, playerUUID);
            deleteStatement.executeUpdate();

            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO apikeys (playerUUID, apikey) VALUES (?, ?);");

            insertStatement.setString(1, playerUUID);
            insertStatement.setString(2, key);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getKey(String playerUUID) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM apikeys WHERE playerUUID=?;");
            stmt.setString(1, playerUUID);
            ResultSet results = stmt.executeQuery();

            String key = "";

            while (results.next()) {
                key = results.getString("apikey");
            }

            return key;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean validateKey(String key) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM apikeys WHERE apikey=?;");
            stmt.setString(1, key);
            ResultSet results = stmt.executeQuery();

            boolean valid = false;
            while (results.next()) {
                valid = true;
            }

            return valid;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}