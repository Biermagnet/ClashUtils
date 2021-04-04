package de.michi.clashutils.mysql;

import de.michi.clashutils.ClashUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ConfigManager {


    private HashMap<String, String> settings;
    private MySQL mysql;

    public ConfigManager(MySQL mysql) {
        settings = new HashMap<>();
        this.mysql = mysql;
        initialiseConfig();

    }

    public void initialiseConfig() {
        try {
            Statement st = mysql.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM settings");
            while (rs.next()) {
                settings.put(rs.getString("name").toLowerCase(), rs.getString("config"));
            }
            st.close();
        } catch (SQLException throwables) {
            ClashUtils.getLogger().error("Error while trying to load settings: " + throwables.getMessage());
        }

    }

    public String getConfig(String name) {
        if (settings.containsKey(name)) {
            return settings.get(name);
        }
        return null;
    }

    public boolean setConfig(String name, String config) {
        try {
            PreparedStatement st = mysql.getConnection().prepareStatement("INSERT INTO settings (name, config) VALUES (?, ?)" +
                    " ON DUPLICATE KEY UPDATE config = ?");
            st.setString(1, name);
            st.setString(2, config);
            st.setString(3, config);
            st.executeUpdate();
            st.close();
            settings.remove(name);
            settings.put(name, config);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public HashMap<String, String> getAllConfigs() {
        HashMap<String, String> config = new HashMap<>();
        try {
            Statement st = mysql.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM settings");
            while (rs.next()) {
                config.put(rs.getString("name"), rs.getString("config"));
            }
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return config;
    }


}
