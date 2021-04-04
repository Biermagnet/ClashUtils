package de.michi.clashutils.input.listener;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.input.commands.Command;
import de.michi.clashutils.input.commands.CommandPermission;
import de.michi.clashutils.input.commands.HelpPage;
import de.michi.clashutils.mysql.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class CommandListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        User author = e.getAuthor();
        if (author.isBot()) return;
        String msg = e.getMessage().getContentRaw();
        if (!(msg.toLowerCase().startsWith(ClashUtils.getPrefix()))) return;
        msg = msg.substring(ClashUtils.getPrefix().length()).toLowerCase();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);

        msg = msg.replaceAll("\\s+", " ").trim();
        String token = msg.contains(" ") ? msg.split(" ")[0] : msg;
        if (token.equalsIgnoreCase("help")) {
            HelpPage.printHelp(1, e.getChannel());
            return;
        }
        PermissionManager pm = ClashUtils.getPermissionManager();
        for (Command c : ClashUtils.getCommandManager().getAllCommands()) {
            if (c.getName().equalsIgnoreCase(token) || c.getAlias().equalsIgnoreCase(token)) {

                CommandPermission userPerm = CommandPermission.getPermissionByID(pm.getUserPermission(e.getAuthor().getId()));
                if (!e.getGuild().getOwnerId().equalsIgnoreCase(e.getAuthor().getId())) {
                    if (!c.getPermission().hasPermission(userPerm.getValue())) {
                        eb.setColor(Color.red);
                        eb.setTitle("You do not have enough permissions for this command.");
                        eb.setDescription("You need to have the permission " + c.getPermission().name() + ".");
                        e.getChannel().sendMessage(eb.build()).queue();
                        return;
                    }
                }
                String[] oldArgs = msg.split(" |\n");

                int n = oldArgs.length - 1;
                String[] args = new String[n];
                System.arraycopy(oldArgs, 1, args, 0, n);

                if (c.getExactArgs() != -1 && c.getExactArgs() != args.length) {
                    eb.setTitle("Wrong amount of arguments provided.");
                    eb.setDescription("This command requires **" + c.getExactArgs() + " arguments.**\nType **"
                            + ClashUtils.getPrefix() + "help " + token + "** for further information.");
                    e.getChannel().sendMessage(eb.build()).queue();
                    return;
                }


                if (c.getMinArgs() != -1 && c.getMinArgs() > args.length) {
                    eb.setTitle("Not enough arguments provided");
                    eb.setDescription("This command requires **at least " + c.getMinArgs() + " arguments.**\nType **"
                            + ClashUtils.getPrefix() + "help " + token + "** for further information.");
                    e.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
                if (c.getMaxArgs() != -1 && c.getMaxArgs() < args.length) {
                    eb.setTitle("Too many arguments provided.");
                    eb.setDescription("This command requires **not more than " + c.getMaxArgs() + " arguments.**\nType **"
                            + ClashUtils.getPrefix() + "help " + token + "** for further information.");
                    e.getChannel().sendMessage(eb.build()).queue();
                    return;
                }


                c.setE(e);
                c.setArgs(args);
                c.setUserPermission(userPerm);

                try {
                    CompletableFuture.runAsync(() -> {
                        c.execute();
                    });
                } catch (Exception ex) {
                    ClashUtils.getLogger().error("Error while trying to execute command " + token);
                    eb.setDescription("Error while trying to execute this command.\nPlease contact the dev for help.");
                    e.getChannel().sendMessage(eb.build()).queue();
                    ex.printStackTrace();
                }

                return;
            }
        }

        eb.setTitle("This command does not exist.");
        eb.setDescription("Type **" + ClashUtils.getPrefix() + "help** for further information.");
        e.getChannel().sendMessage(eb.build()).queue();
        // invalid commands

    }
}
