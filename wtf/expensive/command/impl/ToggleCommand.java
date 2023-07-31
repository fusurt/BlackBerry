package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "t", description = "�������� ������")
public class ToggleCommand extends Command {

    @Override
    public void run(String[] args) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (Expensive.getInstance().getModuleManager().get(moduleName) != null) {
                Expensive.getInstance().getModuleManager().get(moduleName).toggle();
                sendMessage("������ " + ChatFormatting.GRAY +  moduleName.toUpperCase() + ChatFormatting.RESET + " " + (Expensive.getInstance().getModuleManager().get(moduleName).state ? "�������" : "��������"));
            } else {
                sendMessage("������ " + ChatFormatting.GRAY +  moduleName.toUpperCase() + ChatFormatting.RESET + " �� ������");
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.RED + "�������������: " + ChatFormatting.GRAY + ".t <������>");
    }
}
