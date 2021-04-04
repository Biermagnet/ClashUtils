package de.michi.clashutils.input.commands;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.utils.MessageSplitter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HelpPage {

    private static HashMap<Integer, String> pages;


    public static void printHelp(int page, MessageChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Help page " + page);
        eb.setDescription(pages.get(page));
        eb.setColor(Color.cyan);
        channel.sendMessage(eb.build()).queue();

    }

    public static void updateHelpPages() {
        pages = new HashMap<>();
        ArrayList<Command> commands = ClashUtils.getCommandManager().getAllCommands();
        MessageSplitter splitter = new MessageSplitter(0);
        for (Command c : commands) {
            String content = "**" + c.getName() + "  |  " + c.getAlias() + "**\n" + c.getDescription() + "\n\n";
            splitter.add(content);
        }
        splitter.add("**Type " + ClashUtils.getPrefix() + "help <command> for more information.**");

        ArrayList<String> messages = splitter.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            pages.put((i + 1), messages.get(i));
        }
    }
}
