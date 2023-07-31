package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import wtf.expensive.Expensive;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;
import wtf.expensive.config.Config;
import wtf.expensive.config.ConfigManager;

import java.io.File;
import java.util.Objects;

@CommandAnnotation(name = "cfg", description = "��������� ��������� ������")
public class CommandCfg extends Command {
    @Override
    public void run(String[] args) {
        if (args.length >= 2) {
            try {
                if (args[1].equals("save")) {
                    Expensive.getInstance().config.saveConfig(args[2]);
                    sendMessage("������������ " + ChatFormatting.GRAY + args[2] + ChatFormatting.RESET + " ���������.");
                }
                if (args[1].equals("load")) {
                    if (Expensive.getInstance().config.loadConfig(args[2])) {
                        sendMessage("������������ " + ChatFormatting.GRAY + args[2] + ChatFormatting.RESET + " ���������.");
                    } else {
                        sendMessage("������������ " + ChatFormatting.GRAY + args[2] + ChatFormatting.RESET + " �� ���� ���������. (������ �����, � ������ ����)");
                    }
                }
                if (args[1].equals("delete")) {
                    Expensive.getInstance().config.deleteConfig(args[2]);
                    sendMessage("������������ " + ChatFormatting.GRAY + args[2] + ChatFormatting.RESET + " �������.");
                }
                if (args[1].equals("list")) {

                    if (ConfigManager.getLoadedConfigs().isEmpty()) {
                        sendMessage("������������ �� �������.");
                    }
                    for (Config config : Expensive.getInstance().config.getContents()) {
                        sendMessage(config.getName());
                    }
                }

                if (args[1].equals("dir")) {
                    File file = new File(Minecraft.getMinecraft().mcDataDir, "\\expensive\\configs");
                    Sys.openURL(file.getAbsolutePath());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + "." + "cfg load " + ChatFormatting.GRAY + "<name>");
        sendMessage(ChatFormatting.WHITE + "." + "cfg save " + ChatFormatting.GRAY + "<name>");
        sendMessage(ChatFormatting.WHITE + "." + "cfg delete " + ChatFormatting.GRAY + "<name>");
        sendMessage(ChatFormatting.WHITE + "." + "cfg list" + ChatFormatting.GRAY
                + " - �������� ������ ��������");
        sendMessage(ChatFormatting.WHITE + "." + "cfg dir" + ChatFormatting.GRAY
                + " - ������� ����� � ���������");
    }
}
