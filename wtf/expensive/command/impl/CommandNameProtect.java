package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.utility.util.ChatUtility;

@CommandAnnotation(name = "nameprotect", description = "��������� ��������� ��������� ���")
public class CommandNameProtect extends Command {
    public static String old, current;
    public static boolean canChange;

    @Override
    public void run(String[] args) {
        if (args.length >= 2) {
            old = "�6Protected";

            if (args[1].equalsIgnoreCase("set")) {

                current = args[2];
                canChange = true;
                ChatUtility.addChatMessage("�a������� �f������� ��� �� �c" + current);
            }
            if (args[1].equalsIgnoreCase("reset")) {
                current = old;
                canChange = false;
                ChatUtility.addChatMessage("������� �������� ���!");

            }
        } else error();
    }

    @Override
    public void error() {
        ChatUtility.addChatMessage(ChatFormatting.RED + "�������������: " + ChatFormatting.GRAY + ".nameprotect <set/reset> <name>");
    }
}

