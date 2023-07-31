package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.modules.ModuleAnnotation;

@CommandAnnotation(name = "friend", description = "��������� ����� � ������")
public class CommandFriend extends Command {

    @Override
    public void run(String[] args) {
        switch (args[1]) {
            case "add" -> {

                if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().session.getUsername())) {
                    sendMessage(ChatFormatting.WHITE + "��� �� �������� ��������� �������� ��� ���� � ������?");
                } else {
                    if (Expensive.getInstance().friendManager.getFriends().contains(args[2])) {
                        sendMessage(ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ��� ���� � ������ ������");
                    } else {
                        Expensive.getInstance().friendManager.addFriend(args[2]);
                        sendMessage(ChatFormatting.GRAY + "������� ������� " + ChatFormatting.RED + args[2]
                                + ChatFormatting.GRAY + " � ������");
                    }
                }
            }
            case "remove" -> {
                if (Expensive.getInstance().friendManager.isFriend(args[2])) {
                    Expensive.getInstance().friendManager.removeFriend(args[2]);
                    sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " ��� ������ �� ������ ������");
                } else {
                    sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " ���� � ������ ������");
                }
            }
            case "clear" -> {
                if (Expensive.getInstance().friendManager.getFriends().isEmpty()) {
                    sendMessage("������ ������ ����");
                } else {
                    Expensive.getInstance().friendManager.clearFriend();
                    sendMessage("������ ������ ������� ������");
                }
            }
            case "list" -> {
                if (Expensive.getInstance().friendManager.getFriends().isEmpty()) {
                    sendMessage("������ ������ ����");
                    return;
                }
                sendMessage(ChatFormatting.GREEN + "������ ������: ");
                Expensive.getInstance().friendManager.getFriends().forEach(friend -> sendMessage(friend.getName()));
            }
        }
    }
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + "." + "friend add " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "friend remove " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "friend list" + ChatFormatting.GRAY + " - ���������� ������ ���� ��������");
        sendMessage(ChatFormatting.WHITE + "." + "friend clear" + ChatFormatting.GRAY + " - ������� ���� ��������");
    }
}
