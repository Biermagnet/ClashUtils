package de.michi.clashutils.input.commands;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.mysql.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class PermissionCommand extends Command {


    public PermissionCommand() {
        super("permission");
        setAlias("perm");
        setDescription("Manage the permissions of the users.");
    }

    @Override
    public void execute() {
        String[] args = getArgs();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.cyan);

        if (args.length == 0) {
            int perm = ClashUtils.getPermissionManager().getUserPermission(getEvent().getAuthor().getId());
            CommandPermission name = CommandPermission.getPermissionByID(perm);
            eb.setDescription("You have the permission **" + name + " (" + name.getValue() + ")**.");
            getChannel().sendMessage(eb.build()).queue();
            return;
        }

        if (getUserPermission().hasPermission(CommandPermission.ADMIN)) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    for (int i = 0; i < 5; i++) {
                        eb.appendDescription(CommandPermission.getPermissionByID(i) + " | " + i + "\n");
                    }
                    eb.setTitle("All permissions");
                    getChannel().sendMessage(eb.build()).queue();
                }
            } else if (args.length == 3) {
                //set id perm
                if (args[0].equalsIgnoreCase("set")) {
                    Integer id;
                    try {
                        id = Integer.valueOf(args[2]);
                    } catch (Exception e) {
                        eb.setColor(Color.red);
                        eb.setTitle("Please enter a valid permission (0-4)");
                        getChannel().sendMessage(eb.build()).queue();
                        return;
                    }

                    if (id > 4 || id < 0) {
                        eb.setColor(Color.red);
                        eb.setTitle("Please enter a valid permission (0-4)");
                        getChannel().sendMessage(eb.build()).queue();
                        return;
                    }

                    User user = ClashUtils.getJda().getUserById(args[1]);
                    if (user == null) {
                        eb.setColor(Color.red);
                        eb.setTitle("The user with the id " + args[1] + " could not be found.");
                        getChannel().sendMessage(eb.build()).queue();
                        return;
                    }
                    PermissionManager pm = ClashUtils.getPermissionManager();
                    if (pm.setUserPermission(args[1], id)) {
                        eb.setColor(Color.green);
                        eb.setTitle("Successfully set the permission of " + user.getAsMention() + " to "
                                + CommandPermission.getPermissionByID(id) + " (" + id + ")");
                    } else {
                        eb.setColor(Color.red);
                        eb.setTitle("Error occured while trying to update the permission of " + user.getAsMention() +
                                "\nPlease report this to the dev.");

                    }
                    getChannel().sendMessage(eb.build()).queue();
                    return;
                }

                printCommandDoesNotExistMessage();
            }
        } else {
            printNotEnoughPermissionMessage(CommandPermission.ADMIN);
            return;
        }

    }
}
