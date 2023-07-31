package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "way", description = "Прокладывает путь до координат")
public class CommandWay extends Command {

    public static int x, z;

    public static boolean way = false;

    @Override
    public void run(String[] args) {

        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("nether")) {
                if (args.length == 4) {
                    way = true;
                    x = Integer.parseInt(args[2]) / 8;
                    z = Integer.parseInt(args[3]) / 8;
                    sendMessage(x + " " + z);
                } else {
                    sendMessage(ChatFormatting.GRAY + "Укажите только " + ChatFormatting.RED + "X" + ChatFormatting.GRAY + "-" + ChatFormatting.RED + "Z" + ChatFormatting.GRAY + " координаты");
                }
            }
            if (args[1].equalsIgnoreCase("off")) {
                x = z = 0;
                way = false;
            } else if (args.length == 3) {
                way = true;
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);

            } else if (!args[1].equalsIgnoreCase("nether")) {
                sendMessage(ChatFormatting.GRAY + "Укажите только " + ChatFormatting.RED + "X" + ChatFormatting.GRAY + "-" + ChatFormatting.RED + "Z" + ChatFormatting.GRAY + " координаты");
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + ".way " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "x, z" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + ".way nether " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "x, z" + ChatFormatting.GRAY + ">" + " (прокладывает путь в ад из обычного мира)");
        sendMessage(ChatFormatting.WHITE + ".way " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "off" + ChatFormatting.GRAY + ">");
    }
}
