package wtf.expensive.command;

import wtf.expensive.command.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commandList = new ArrayList<>();

    public CommandManager() {
        commandList.add(new CommandBind());
        commandList.add(new CommandWay());
        commandList.add(new CommandHelp());
        commandList.add(new CommandTP());
        commandList.add(new CommandCfg());
        commandList.add(new CommandLogin());
        commandList.add(new CommandFriend());
        commandList.add(new CommandStaff());
        commandList.add(new CommandNameProtect());
        commandList.add(new MacroCommand());
        commandList.add(new CommandHClip());
        commandList.add(new CommandVClip());
        commandList.add(new CommandRCT());
        commandList.add(new ToggleCommand());
    }

    public List<Command> getCommands() {
        return commandList;
    }

}
