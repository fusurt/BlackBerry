package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "tp", description = "��������� ����������������� � ����� �����")
public class CommandTP extends Command {

    @Override
    public void run(String[] args) {
        if (args.length >= 2) {
            double x = 0, y = 0, z = 0;
            if (args.length == 4) {
                x = Double.parseDouble(args[1]);
                y = Double.parseDouble(args[2]);
                z = Double.parseDouble(args[3]);
                sendMessage( "������� ����������������� �� " + ChatFormatting.LIGHT_PURPLE + args[1] + " "  + args[2]  + " " + args[3]);
            } else if (args.length == 3) {
                x = Double.parseDouble(args[1]);
                y = 150;
                z = Double.parseDouble(args[2]);
                sendMessage("������� ����������������� �� " + ChatFormatting.LIGHT_PURPLE + args[1] + " "  + args[2]);
            } else if (args.length == 2) {
                x = mc.player.posX;
                y = mc.player.posY + Double.parseDouble(args[1]);
                z = mc.player.posZ;
                sendMessage(ChatFormatting.GREEN + "�� ������� ����������������� �� " + ChatFormatting.WHITE + args[1] + ChatFormatting.GREEN + " ������ �����");
            } else {
                error();
            }
            for (int i = 0; i < 24; i++) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
            }
            for (int i = 0; i < 24; i++) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, y, mc.player.posZ, true));
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");

        sendMessage(".tp" + ChatFormatting.GRAY + " <" + "x" + ChatFormatting.GRAY + "> " + ChatFormatting.GRAY + "<" + "y" + ChatFormatting.GRAY + "> " + ChatFormatting.GRAY + "<" + "z" + ChatFormatting.GRAY + ">");

        sendMessage(".tp" + ChatFormatting.GRAY + " <" + "x" + ChatFormatting.GRAY + "> " + ChatFormatting.GRAY + "<" + "z" + ChatFormatting.GRAY + ">");

        sendMessage(".tp" + ChatFormatting.GRAY + " <" + "y" + ChatFormatting.GRAY + ">");
    }
}
