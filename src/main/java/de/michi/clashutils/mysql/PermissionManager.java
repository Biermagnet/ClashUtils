package de.michi.clashutils.mysql;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.input.commands.CommandPermission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class PermissionManager {


    private MySQL mysql;
    private HashMap<String, Integer> playerPerms;
    private HashMap<String, Integer> channelPerms;

    public PermissionManager(MySQL mySQL) {
        this.mysql = mySQL;
        playerPerms = new HashMap<>();
        channelPerms = new HashMap<>();
    }

    public boolean userHasPermssion(String userid, CommandPermission needed) {
        return needed.hasPermission(getUserPermission(userid));
    }

    public int getUserPermission(String userid) {
        try {
            Statement st = mysql.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT permission FROM user_permissions WHERE userid = '" + userid + "'");
            if(rs.next()) {
                return rs.getInt("permission");
            }else {
                return 0;
            }
        } catch (SQLException throwables) {
            ClashUtils.getLogger().error("Error while trying to get user permission: " + throwables.getMessage());
            return 0;
        }
    }

    public boolean setUserPermission(String userid, int perm) {
        try {
            Statement st = mysql.getConnection().createStatement();
            st.executeUpdate("INSERT INTO user_permissions (userid, permission) VALUES ('" + userid + "', '" + perm + "')" +
                    " ON DUPLICATE KEY UPDATE permission = " + perm);
            st.close();
            return true;
        } catch (SQLException throwables) {
            ClashUtils.getLogger().error("Error while trying to set user permission: " + throwables.getMessage());
            return false;
        }
    }
}
