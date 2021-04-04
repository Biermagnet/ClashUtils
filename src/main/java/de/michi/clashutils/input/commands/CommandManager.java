package de.michi.clashutils.input.commands;


import java.util.ArrayList;

public class CommandManager {

    private ArrayList<Command> commands;

    public CommandManager() {
        commands = new ArrayList<>();
    }


    public void add(Command cmd) {
        commands.add(cmd);
        HelpPage.updateHelpPages();
    }

    public ArrayList<Command> getAllCommands() {
        return commands;
    }
}
