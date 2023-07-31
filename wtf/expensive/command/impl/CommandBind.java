package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.modules.Module;

@CommandAnnotation(name = "bind", description = "Позволяет забиндить модуль на определенную клавишу")
public class CommandBind extends Command {

    @Override
    public void run(String[] args) {
        try {
            if (args.length >= 2) {

                if (args[1].equalsIgnoreCase("list")) {
                    sendMessage(ChatFormatting.GRAY + "Список всех модулей с привязанными клавишами:");
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        if (f.bind == 0) continue;
                        sendMessage(f.name + " [" + ChatFormatting.GRAY + Keyboard.getKeyName(f.bind) + ChatFormatting.RESET + "]");
                    }
                }

                if (args[1].equalsIgnoreCase("clear")) {
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        f.bind = 0;
                    }
                    sendMessage(ChatFormatting.GREEN + "Все клавиши были отвязаны от модулей");
                }

                String moduleName = args[2], keyName = args[3].toUpperCase();

                int key = Keyboard.getKeyIndex(args[3].toUpperCase());

                Module module = Expensive.getInstance().getModuleManager().get(moduleName);

                if (args[1].startsWith("add")) {
                    if (module != null) {
                        module.bind = key;
                        sendMessage("Клавиша " + ChatFormatting.GRAY + keyName
                                + ChatFormatting.WHITE + " была привязана к модулю " + ChatFormatting.GRAY + module.name);
                    } else {
                        sendMessage("Модуль " + moduleName + " не был найден");
                    }
                }

                if (args[1].startsWith("remove")) {
                    for (Module f : Expensive.getInstance().getModuleManager().getModules()) {
                        if (f.name.equalsIgnoreCase(moduleName)) {
                            f.bind = 0;
                            sendMessage("Клавиша " + ChatFormatting.GRAY + keyName + ChatFormatting.RESET + " была отвязана от модуля " + ChatFormatting.GRAY + f.name);
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
        sendMessage(ChatFormatting.WHITE + "Неверный синтаксис команды. " + ChatFormatting.GRAY + "Используйте:");
        sendMessage(ChatFormatting.WHITE + ".bind add " + ChatFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(ChatFormatting.WHITE + ".bind remove " + ChatFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(ChatFormatting.WHITE + ".bind list");
        sendMessage(ChatFormatting.WHITE + ".bind clear");
    }
}
