package de.michi.clashutils.test;

import de.michi.clashutils.ClashUtils;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;

public class test {

    public static void main(String... args) throws LoginException, InterruptedException {
        ClashUtils utils = new ClashUtils(args[0], args[1], args[2]);
        utils.setMySQLData("clashutils", "localhost", "root", "");
        utils.getLogger().addOutputChannel("827864343702011909");
        utils.setPrefix("kb ");
        MessageChannel channel = utils.getJda().awaitReady().getTextChannelById("825317684085522474");
        utils.getCommandManager().add(new TestCommand());

    }
}
