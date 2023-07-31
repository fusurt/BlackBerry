package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.command.macro.Macro;

@CommandAnnotation(name = "macro", description = "Позволяет отправить команду по нажатию кнопки")

public class MacroCommand extends Command {
    @Override
    public void run(String[] args) {
        if (args.length > 1) {
            switch (args[1]) {
                case "add" -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    Expensive.getInstance().macroManager.addMacros(new Macro(sb.toString(), Keyboard.getKeyIndex(args[2].toUpperCase())));
                    sendMessage(ChatFormatting.GREEN + "Добавлен макрос для кнопки" + ChatFormatting.RED + " \""
                            + args[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "с командой "
                            + ChatFormatting.RED + sb);
                }
                case "clear" -> {
                    if (Expensive.getInstance().macroManager.getMacros().isEmpty()) {
                        sendMessage(ChatFormatting.RED + "Список макросов пуст");
                    } else {
                        sendMessage(ChatFormatting.GREEN + "Список Макросов " + ChatFormatting.WHITE + "успешно очищен");
                        Expensive.getInstance().macroManager.getMacros().clear();
                        Expensive.getInstance().macroManager.updateFile();
                    }
                }
                case "remove" -> {
                    Expensive.getInstance().macroManager.deleteMacro(Keyboard.getKeyIndex(args[2].toUpperCase()));
                    sendMessage(ChatFormatting.GREEN + "Макрос " + ChatFormatting.WHITE + "был удален с кнопки "
                            + ChatFormatting.RED + "\"" + args[2] + "\"");
                }
                case "list" -> {
                    if (Expensive.getInstance().macroManager.getMacros().isEmpty()) {
                        sendMessage("Список макросов пуст");
                    } else {
                        sendMessage(ChatFormatting.GREEN + "Список макросов: ");
                        Expensive.getInstance().macroManager.getMacros()
                                .forEach(macro -> sendMessage(ChatFormatting.WHITE + "Команда: " + ChatFormatting.RED
                                        + macro.getMessage() + ChatFormatting.WHITE + ", Кнопка: " + ChatFormatting.RED
                                        + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            }
        } else {
            error();
        }
    }
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + "." + "macro add " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "key" + ChatFormatting.GRAY + ">" + ChatFormatting.GRAY + " message");
        sendMessage(ChatFormatting.WHITE + "." + "macro remove " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "key" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "macro list");
        sendMessage(ChatFormatting.WHITE + "." + "macro clear");
    }
}
