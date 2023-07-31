package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

import java.util.ArrayList;
import java.util.List;

@CommandAnnotation(name = "staff", description = "Добавляет ник в Staff Statistic")

public class CommandStaff extends Command {

    public static List<String> staffNames = new ArrayList<>();

    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + ".staff " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "add; remove; clear; list." + ChatFormatting.GRAY + ">");
    }

    @Override
    public void run(String[] args) {
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (staffNames.contains(args[2])) {
                    sendMessage(ChatFormatting.RED + "Этот игрок уже в Staff List!");
                } else {
                    staffNames.add(args[2]);
                    sendMessage(ChatFormatting.GREEN + "Ник " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " добавлен в Staff List");
                }
            }
            if (args[1].equalsIgnoreCase("remove")) {
                if (staffNames.contains(args[2])) {
                    staffNames.remove(args[2]);
                    sendMessage(ChatFormatting.GREEN + "Ник " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " удален из Staff List");
                } else {
                    sendMessage(ChatFormatting.RED + "Этого игрока нет в Staff List!");
                }
            }
            if (args[1].equalsIgnoreCase("clear")) {
                if (staffNames.isEmpty()) {
                    sendMessage(ChatFormatting.RED + "Staff List пуст!");
                } else {
                    staffNames.clear();
                    sendMessage(ChatFormatting.GREEN + "Staff List очищен");
                }
            }
            if (args[1].equalsIgnoreCase("list")) {
                sendMessage(ChatFormatting.GRAY + "Список Staff:");
                for (String name : staffNames) {
                    sendMessage(ChatFormatting.WHITE + name);
                }
            }

        } else {
            error();
        }
    }
}
