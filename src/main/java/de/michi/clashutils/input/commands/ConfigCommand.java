package de.michi.clashutils.input.commands;

import de.michi.clashutils.ClashUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;

public class ConfigCommand extends Command {


    public ConfigCommand() {
        super("config");
        setMinArgs(1);
        setAlias("cfg");
        setPermission(CommandPermission.ADMIN);
        setDescription("Change the configurations for the bot");
    }

    @Override
    public void execute() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.cyan);
        if (getArgs()[0].equalsIgnoreCase("list")) {
            HashMap<String, String> config = ClashUtils.getConfigManager().getAllConfigs();
            eb.setTitle("All configs");
            for (String name : config.keySet()) {
                eb.addField(name, config.get(name), false);
            }
            getEvent().getChannel().sendMessage(eb.build()).queue();
        } else if (getArgs()[0].equalsIgnoreCase("get")) {
            eb.setTitle("Config of " + getArgs()[1]);
            String config = ClashUtils.getConfigManager().getConfig(getArgs()[1]);
            eb.setDescription("The config of " + getArgs()[1] + " is " + config + ".");
            if (config == null) {
                eb.setDescription("This config does not exist.");
            }
            getEvent().getChannel().sendMessage(eb.build()).queue();
        } else if (getArgs()[0].equalsIgnoreCase("set")) {
            boolean success = ClashUtils.getConfigManager().setConfig(getArgs()[1], getArgs()[2]);
            if (success) {
                eb.setDescription("Successfully set the config of " + getArgs()[1] + " to " + getArgs()[2]);
            } else {
                eb.setDescription("An error occured while trying to update the config. Please report this to the dev.");
            }
            getEvent().getChannel().sendMessage(eb.build()).queue();
        }

    }
}
