package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.math.NumberUtils;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "vclip", description = "��������� ����������������� �� ���������")

public class CommandVClip extends Command {

    @Override
    public void run(String[] args) {
        float y = 0;
        if (args[1].equals("down")) {
            for (int i = 0; i < 255; i++) {
                if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = -i - 1;
                    break;
                }
                if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                    sendMessage(ChatFormatting.RED + "��� ����� ����������������� ������ ��� ������.");
                    return;
                }
            }
        }
        if (args[1].equals("up")) {
            for (int i = 10; i < 255; i++) {
                if (mc.world.getBlockState(new BlockPos(mc.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = i + 1;
                    break;
                }
            }
        }
        if (y == 0) {
            if (NumberUtils.isNumber(args[1])) {
                y = Float.parseFloat(args[1]);
            } else {
                sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
                sendMessage(ChatFormatting.RED + args[1] + ChatFormatting.GRAY + " �� �������� ������!");
                return;
            }
        }
        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
        }
        mc.player.connection
                .sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ, false));
        mc.player.setPosition(mc.player.posX, mc.player.posY + y , mc.player.posZ);
    }
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + ".vclip " + ChatFormatting.GRAY + "<" + "value" + ChatFormatting.GRAY + ">");
    }
}
