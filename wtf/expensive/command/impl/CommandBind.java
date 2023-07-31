package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.modules.Module;

@CommandAnnotation(name = "bind", description = "��������� ��������� ������ �� ������������ �������")
public class CommandBind extends Command {

    @Override
    public void run(String[] args) {
        try {
            if (args.length >= 2) {

                if (args[1].equalsIgnoreCase("list")) {
                    sendMessage(ChatFormatting.GRAY + "������ ���� ������� � ������������ ���������:");
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        if (f.bind == 0) continue;
                        sendMessage(f.name + " [" + ChatFormatting.GRAY + Keyboard.getKeyName(f.bind) + ChatFormatting.RESET + "]");
                    }
                }

                if (args[1].equalsIgnoreCase("clear")) {
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        f.bind = 0;
                    }
                    sendMessage(ChatFormatting.GREEN + "��� ������� ���� �������� �� �������");
                }

                String moduleName = args[2], keyName = args[3].toUpperCase();

                int key = Keyboard.getKeyIndex(args[3].toUpperCase());

                Module module = Expensive.getInstance().getModuleManager().get(moduleName);

                if (args[1].startsWith("add")) {
                    if (module != null) {
                        module.bind = key;
                        sendMessage("������� " + ChatFormatting.GRAY + keyName
                                + ChatFormatting.WHITE + " ���� ��������� � ������ " + ChatFormatting.GRAY + module.name);
                    } else {
                        sendMessage("������ " + moduleName + " �� ��� ������");
                    }
                }

                if (args[1].startsWith("remove")) {
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        if (f.name.equalsIgnoreCase(moduleName)) {
                            f.bind = 0;
                            sendMessage("������� " + ChatFormatting.GRAY + keyName + ChatFormatting.RESET + " ���� �������� �� ������ " + ChatFormatting.GRAY + f.name);
                        }
                    }
                }

            } else error();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.WHITE + "�������� ��������� �������. " + ChatFormatting.GRAY + "�����������:");
        sendMessage(ChatFormatting.WHITE + ".bind add " + ChatFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(ChatFormatting.WHITE + ".bind remove " + ChatFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(ChatFormatting.WHITE + ".bind list");
        sendMessage(ChatFormatting.WHITE + ".bind clear");
    }
}
