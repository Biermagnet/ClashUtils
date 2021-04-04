package de.michi.clashutils.utils;

import de.michi.clashutils.clashofclans.Clan;
import de.michi.clashutils.clashofclans.ClanWar;
import de.michi.clashutils.clashofclans.ClanWarState;
import de.michi.clashutils.clashofclans.Player;
import de.michi.clashutils.exceptions.InvalidClanException;
import de.michi.clashutils.exceptions.InvalidPlayerException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.ZoneId;

public class PrintHelper {

    public static void printPlayerInfo(String playertag, MessageChannel channel) {
        Player p;
        try {
            p = new Player(playertag);
        } catch (InvalidPlayerException e) {
            channel.sendMessage(e.getMessage());
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Information");
        eb.addField("Name", p.getName(), false);
        eb.addField("Tag", "[" + p.getTag() + "](https://link.clashofclans.com/en?action=OpenPlayerProfile&tag=" + p.getTag().replace("#", "") + ")", false);
        eb.addField("Townhall", String.valueOf(p.getTownhallLevel()), false);
        eb.addField("Level", String.valueOf(p.getExpLevel()), false);
        if (p.hasClan()) {
            eb.addField("Clan name", p.getClanName(), false);
            eb.addField("Clan tag", "[" + p.getClanTag() + "](https://link.clashofclans.com/en?action=OpenClanProfile&tag="
                    + p.getClanTag().replace("#", "") + ")", false);
            eb.addField("Clan role", p.getClanRole().name().substring(0, 1) + p.getClanRole().name().substring(1).toLowerCase(), false);
        } else {
            eb.addField("Clan", "The player has no clan.", false);
        }
        eb.setColor(Color.cyan);
        channel.sendMessage(eb.build()).queue();
    }

    public static void printClanInfo(String clantag, MessageChannel channel) {
        Clan c;

        try {
            c = new Clan(clantag);
        } catch (InvalidClanException e) {
            channel.sendMessage(e.getMessage()).queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.cyan);
        eb.setThumbnail(c.getMediumIconURL());
        eb.setTitle("Clan information");
        eb.addField("Name", c.getName(), false);
        eb.addField("Tag", "[" + c.getTag() + "](https://link.clashofclans.com/en?action=OpenClanProfile&tag="
                + c.getTag().replace("#", "") + ")", false);
        eb.addField("Level", String.valueOf(c.getClanLevel()), false);
        eb.addField("Location", c.getLocationName(), false);
        eb.addField("Members", String.valueOf(c.getMemberAmount()), false);
        channel.sendMessage(eb.build()).queue();
    }

    public static void printWarResults(String clantag, MessageChannel channel, @Nullable ZoneId zoneid) {
        try {
            ClanWar war = new ClanWar(clantag);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.cyan);
            if (zoneid != null) war.setTimeZone(zoneid);
            String status = "";
            if (war.getState().equals(ClanWarState.ENDED)) {
                status = "The war has ended " + (war.getWarEndDays() != 0 ? war.getWarEndDays() + "d" : "") + war.getWarEndHours() + "h " + war.getWarEndMinutes() + "m.\n";
                status += "End time: " + war.getFormattedWarEndDate();
            } else if (war.getState().equals(ClanWarState.IN_WAR)) {
                status = "The war ends in " + war.getRemainingWarHours() + "h " + war.getRemainingWarMinutes() + "m.\n";
                status += "**End time: **" + war.getFormattedWarEndDate();
            } else if (war.getState().equals(ClanWarState.PREPARATION)) {
                status = "The war begins in " + war.getRemainingPrepHours() + "h " + war.getRemainingPrepMinutes() + "m.\n";
                status += "Start time: " + war.getFormattedWarStartDate();
            } else if (war.getState().equals(ClanWarState.NULL)) {
                eb.setTitle("No war has been found.");
                channel.sendMessage(eb.build()).queue();
                return;
            }
            eb.setThumbnail(war.getMediumIconURL());
            eb.setTitle("War of " + war.getName());
            eb.setFooter("Timezone: " + war.getZoneID());
            eb.addField(war.getName() + " vs. " + war.getOpponentName(), status, false);
            eb.addField("Prep/war length", (war.getPrepHours() != 0 ? war.getPrepHours() + "h / " : war.getPrepMinutes() + "m / ")
                    + (war.getWarHours() != 0 ? war.getWarHours() + "h" : war.getWarMinutes() + "m"), false);
            eb.addField("Breakdown", war.getBreakdown().equalsIgnoreCase(war.getopponentBreakdown()) ? war.getBreakdown() : war.getBreakdown() + "  -  " + war.getopponentBreakdown(), false);

            String info = war.getStars() + "⭐" + spacesToMiddle(war.getStars() + "⭐") + "Stars" + spacesToMiddle("Stars") + war.getOpponentStars() + "⭐ " + spacesAtEnd(war.getOpponentStars() + "⭐") + "\n";
            info += war.getDestruction() + "%" + spacesToMiddle(war.getDestruction() + "%") + "Destruction" + spacesToMiddle("Destruction") + war.getOpponentDestruction() + "%" + spacesAtEnd(war.getOpponentDestruction() + "%");
            info += "\n";
            info += "\n";
            info += spacesToMiddle("") + "Hitrates" + spacesToMiddle("Hitrates") + spacesAtEnd("");
            info += "\n";
            info += "--------------------------------------------------";
            info += "\n";
            for (int i = 13; i > 3; i--) {
                if (war.getAttacks(i) != 0) {
                    String first = war.getTripplesAsString(i) + "  " + war.getHitrate(i) + "%";
                    String second = war.getOpponentTripplesAsString(i) + "  " + war.getOpponentHitrate(i) + "%";
                    info += first + spacesToMiddle(first) + "TH" + i + spacesToMiddle("TH" + i) + second + spacesAtEnd(second) + "\n";
                }
            }
            channel.sendMessage(eb.build()).queue();
            channel.sendMessage("```" + info + "```").queue();

        } catch (InvalidClanException e) {
            channel.sendMessage(e.getMessage()).queue();
        }
    }


    public static String spacesToMiddle(String text) {
        String spaces = "";
        for (int i = 0; i < 20 - text.length(); i++) {
            spaces += " ";
        }
        return spaces;
    }

    public static String spacesAtEnd(String text) {
        String spaces = "";
        for (int i = 0; i < 10 - text.length(); i++) {
            spaces += " ";
        }
        return spaces;

    }


}
