package de.michi.clashutils.input.commands;

import de.michi.clashutils.ClashUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public abstract class Command {

    private String name;
    private String alias;
    private CommandPermission permission;
    private CommandPermission userPermission;
    private int minArgs;
    private int maxArgs;
    private int exactArgs;
    private String[] args;
    private GuildMessageReceivedEvent e;
    private String description;
    private String help;


    public Command(String name) {
        this.name = name;
        this.alias = "";
        this.permission = CommandPermission.USER;
        this.minArgs = -1;
        this.maxArgs = -1;
        this.exactArgs = -1;
        this.help = "No help for this command.";
        this.description = "No description for this command.";
    }

    public abstract void execute();

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CommandPermission getPermission() {
        return permission;
    }

    public void setPermission(CommandPermission permission) {
        this.permission = permission;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public void setUserPermission(CommandPermission perm) {
        this.userPermission = perm;
    }

    public CommandPermission getUserPermission() {
        return userPermission;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }

    public int getExactArgs() {
        return exactArgs;
    }

    public void setExactArgs(int exactArgs) {
        this.exactArgs = exactArgs;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public GuildMessageReceivedEvent getEvent() {
        return e;
    }

    public void setE(GuildMessageReceivedEvent e) {
        this.e = e;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public MessageChannel getChannel() {
        return e.getChannel();
    }

    public void printNotEnoughPermissionMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        eb.setTitle("You do not have enough permissions for this command.");
        eb.setDescription("You need to have the permission " + getPermission().name() + ".");
        e.getChannel().sendMessage(eb.build()).queue();
    }

    public void printNotEnoughPermissionMessage(CommandPermission permission) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        eb.setTitle("You do not have enough permissions for this command.");
        eb.setDescription("You need to have the permission " + permission.name() + ".");
        e.getChannel().sendMessage(eb.build()).queue();
    }

    public void printCommandDoesNotExistMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.orange);
        eb.setTitle("This command does not exist.");
        eb.setDescription("Type **" + ClashUtils.getPrefix() + "help " + name + "** for more help.");
        getChannel().sendMessage(eb.build()).queue();
    }
}
