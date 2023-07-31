package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "help", description = "Shows all commands")
public class CommandHelp extends Command {


    @Override
    public void run(String[] args) {
        for (Command command : Expensive.getInstance().commandManager.getCommands()) {
            if (!(command instanceof CommandHelp)) {
                sendMessage(ChatFormatting.WHITE + "." +  command.command + ChatFormatting.GRAY + " ("
                        + ChatFormatting.WHITE + command.description + ChatFormatting.GRAY + ")");
            }
        }
    }
}
