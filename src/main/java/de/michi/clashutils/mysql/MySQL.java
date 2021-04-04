package de.michi.clashutils.mysql;

import de.michi.clashutils.ClashUtils;

import java.sql.*;

public class MySQL {

    private Connection con;
    private String database;
    private String ip;
    private String user;
    private String password;


    public MySQL(String database, String ip, String user, String password) {
        this.database = database;
        this.ip = ip;
        this.user = user;
        this.password = password;
        connect();
        createStandardTables();
    }

    private void connect() {
        String url = "jdbc:mysql://" + ip + "/" + database + "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&useUnicode=yes&characterEncoding=UTF-8";
        try {
            con = DriverManager.getConnection(url, user, password);
            ClashUtils.getLogger().success("Successfully created a connection to the database. WOOHOO");
        } catch (Exception e) {
            ClashUtils.getLogger().error("Error while trying to connect to the database: " + e.getMessage());
        }

    }

    private void createStandardTables() {
        try {
            Statement st = con.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS user_permissions (userid VARCHAR(50), permission TINYINT(3), PRIMARY KEY(userid)) CHARACTER SET utf8 COLLATE utf8_general_ci;");
            st.execute("CREATE TABLE IF NOT EXISTS channel_permissions (channelid VARCHAR(50), permission TINYINT(3), PRIMARY KEY(channelid)) CHARACTER SET utf8 COLLATE utf8_general_ci;");
            st.execute("CREATE TABLE IF NOT EXISTS settings (name VARCHAR(40), config VARCHAR(40), PRIMARY KEY(name)) CHARACTER SET utf8 COLLATE utf8_general_ci;");
        } catch (SQLException e) {
            ClashUtils.getLogger().error("Erorr while trying to create standard tables: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }


}
