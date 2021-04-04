package de.michi.clashutils.test;

import de.michi.clashutils.input.commands.Command;

public class TestCommand extends Command {


    public TestCommand() {
        super("test");
        setAlias("ping");
        setMinArgs(1);
        setMaxArgs(3);
    }

    @Override
    public void execute() {
        getEvent().getChannel().sendMessage("Pong").queue();
    }
}
