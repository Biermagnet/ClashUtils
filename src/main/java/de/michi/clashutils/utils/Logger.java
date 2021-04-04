package de.michi.clashutils.utils;

import de.michi.clashutils.ClashUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.simple.JSONArray;

import java.awt.*;
import java.util.ArrayList;

public class Logger {

    ArrayList<String> logChannels;
    boolean consolePutput;

    public Logger() {
        logChannels = new ArrayList<>();
        consolePutput = true;
    }

    public void addOutputChannel(String channelID) {
        String currentChannel = ClashUtils.getConfigManager().getConfig("outputChannel");
        JSONArray channel = new JSONArray();
        if (currentChannel == null) {
            channel.add(channelID);
            ClashUtils.getConfigManager().setConfig("outputChannel", channel.toJSONString());
        } else {
            logChannels.add(channelID);
            for (int i = 0; i < logChannels.size(); i++) {
                channel.add(logChannels.get(i));
            }
            ClashUtils.getConfigManager().setConfig("outputChannel", channel.toJSONString());
        }
    }

    public void removeOutputChannel(String channelID) {
        if (!logChannels.contains(channelID)) return;
        logChannels.remove(channelID);
        JSONArray array = new JSONArray();
        for (int i = 0; i < logChannels.size(); i++) {
            array.add(logChannels.get(i));
        }
        ClashUtils.getConfigManager().setConfig("outputChannel", array.toJSONString());
    }

    public boolean outputChannelExists(String channelID) {
        return this.logChannels.contains(channelID);
    }

    public ArrayList<String> getLogChannels() {
        return logChannels;
    }

    public void setConsolePutput(boolean consolePutput) {
        this.consolePutput = consolePutput;
    }

    public void error(String message) {
        String date = DateUtils.getCurrentFormattedDate();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(date + " | " + "Error: ");
        eb.setDescription(message);
        eb.setColor(Color.red);
        if (consolePutput) System.out.println(date + " | Error: " + eb.getDescriptionBuilder().toString());
        for (String channelID : logChannels) {
            sendMessage(channelID, eb, date, "Error");
        }
    }

    public void warn(String message) {
        String date = DateUtils.getCurrentFormattedDate();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(date + " | " + "Warning: ");
        eb.setDescription(message);
        eb.setColor(Color.ORANGE);
        if (consolePutput) System.out.println(date + " | Warning: " + eb.getDescriptionBuilder().toString());
        for (String channelID : logChannels) {
            sendMessage(channelID, eb, date, "Warning");
        }
    }

    public void inform(String message) {
        String date = DateUtils.getCurrentFormattedDate();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(date + " | " + "Info: ");
        eb.setDescription(message);
        eb.setColor(Color.YELLOW);
        if (consolePutput) System.out.println(date + " | Info: " + eb.getDescriptionBuilder().toString());
        for (String channelID : logChannels) {
            sendMessage(channelID, eb, date, "Info");
        }
    }

    public void success(String message) {
        String date = DateUtils.getCurrentFormattedDate();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(date + " | " + "Success: ");
        eb.setDescription(message);
        eb.setColor(Color.GREEN);
        if (consolePutput) System.out.println(date + " | Success: " + eb.getDescriptionBuilder().toString());
        for (String channelID : logChannels) {
            sendMessage(channelID, eb, date, "Success");
        }
    }


    private void sendMessage(String channelID, EmbedBuilder eb, String date, String type) {
        try {
            MessageChannel outputChannel = ClashUtils.getJda().getTextChannelById(channelID);
            outputChannel.sendMessage(eb.build()).queue();
        } catch (Exception e) {
            System.out.println("Error while trying to write output to the channel with the ID " + channelID + ".\n" +
                    "This channel has been removed from the output channels.");
            logChannels.remove(channelID);
        }
    }


}
