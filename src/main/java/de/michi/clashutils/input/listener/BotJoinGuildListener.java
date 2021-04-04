package de.michi.clashutils.input.listener;

import de.michi.clashutils.ClashUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class BotJoinGuildListener extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.cyan);
        eb.setTitle(ClashUtils.getName() + " joined the Server!");
        eb.setDescription("Type **" + ClashUtils.getPrefix() + "help** for a list of commands.");
        e.getGuild().getDefaultChannel().sendMessage(eb.build()).queue();
    }


}
