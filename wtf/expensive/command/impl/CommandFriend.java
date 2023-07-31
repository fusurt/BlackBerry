package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.modules.ModuleAnnotation;

@CommandAnnotation(name = "friend", description = "Добавляет людей в друзья")
public class CommandFriend extends Command {

    @Override
    public void run(String[] args) {
        switch (args[1]) {
            case "add" -> {

                if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().session.getUsername())) {
                    sendMessage(ChatFormatting.WHITE + "втф ты серьезно пытаешься добавить сам себя в друзья?");
                } else {
                    if (Expensive.getInstance().friendManager.getFriends().contains(args[2])) {
                        sendMessage(ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " уже есть в списке друзей");
                    } else {
                        Expensive.getInstance().friendManager.addFriend(args[2]);
                        sendMessage(ChatFormatting.GRAY + "Успешно добавил " + ChatFormatting.RED + args[2]
                                + ChatFormatting.GRAY + " в друзья");
                    }
                }
            }
            case "remove" -> {
                if (Expensive.getInstance().friendManager.isFriend(args[2])) {
                    Expensive.getInstance().friendManager.removeFriend(args[2]);
                    sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " был удален из списка друзей");
                } else {
                    sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " нету в списке друзей");
                }
            }
            case "clear" -> {
                if (Expensive.getInstance().friendManager.getFriends().isEmpty()) {
                    sendMessage("Список друзей пуст");
                } else {
                    Expensive.getInstance().friendManager.clearFriend();
                    sendMessage("Список друзей успешно очищен");
                }
            }
            case "list" -> {
                if (Expensive.getInstance().friendManager.getFriends().isEmpty()) {
                    sendMessage("Список друзей пуст");
                    return;
                }
                sendMessage(ChatFormatting.GREEN + "Список друзей: ");
                Expensive.getInstance().friendManager.getFriends().forEach(friend -> sendMessage(friend.getName()));
            }
        }
    }
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + "." + "friend add " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "friend remove " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "friend list" + ChatFormatting.GRAY + " - показывает список всех фриендов");
        sendMessage(ChatFormatting.WHITE + "." + "friend clear" + ChatFormatting.GRAY + " - очищает всех фриендов");
    }
}
