package de.michi.clashutils;

import de.michi.clashutils.clashofclans.DynamicSupercellToken;
import de.michi.clashutils.input.commands.CommandManager;
import de.michi.clashutils.input.commands.ConfigCommand;
import de.michi.clashutils.input.commands.PermissionCommand;
import de.michi.clashutils.input.listener.BotJoinGuildListener;
import de.michi.clashutils.input.listener.CommandListener;
import de.michi.clashutils.mysql.ConfigManager;
import de.michi.clashutils.mysql.MySQL;
import de.michi.clashutils.mysql.PermissionManager;
import de.michi.clashutils.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class ClashUtils {

    private static JDA jda;
    private static String prefix;
    private static Logger logger;
    private static DynamicSupercellToken dynamicSupercellToken;
    private static CommandManager commandManager;
    private static MySQL mySQL;
    private static ConfigManager configManager;
    private static PermissionManager permissionManager;
    private static String name;


    public ClashUtils(String discordToken, String email, String password) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(discordToken);
        jda = builder.build();
        logger = new Logger();
        initialiseListener();
        setPrefix(".");
        commandManager = new CommandManager();
        commandManager.add(new ConfigCommand());
        commandManager.add(new PermissionCommand());
        dynamicSupercellToken = new DynamicSupercellToken(email, password);
        dynamicSupercellToken.createKey();
        name = "ClashUtils";
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ClashUtils.name = name;
    }

    public static JDA getJda() {
        return jda;
    }

    public static String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        ClashUtils.prefix = prefix;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static DynamicSupercellToken getDynamicSupercellToken() {
        return dynamicSupercellToken;
    }

    private static void initialiseListener() {
        getJda().addEventListener(new CommandListener());
        getJda().addEventListener(new BotJoinGuildListener());
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public void setMySQLData(String database, String ip, String user, String password) {
        mySQL = new MySQL(database, ip, user, password);
        configManager = new ConfigManager(mySQL);
        permissionManager = new PermissionManager(mySQL);


    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static PermissionManager getPermissionManager() {
        return permissionManager;
    }
}
