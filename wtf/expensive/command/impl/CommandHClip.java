package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.NumberUtils;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "hclip", description = "��������� ����������������� �� �����������")
public class CommandHClip extends Command {

    @Override
    public void run(String[] args) {
        if (args.length >= 2) {
            try {
                if (NumberUtils.isNumber(args[1])) {
                    float f = mc.player.rotationYaw * 0.017453292F;
                    double speed = Double.parseDouble(args[1]);
                    double x = -(MathHelper.sin(f) * speed);
                    double z = MathHelper.cos(f) * speed;
                    mc.player.setPositionAndUpdate(mc.player.posX + x, mc.player.posY, mc.player.posZ + z);
                } else {
                    sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
                    sendMessage(ChatFormatting.RED + args[1] + ChatFormatting.GRAY + " �� �������� ������!");
                }
            } catch (Exception ignored) {
            }
        } else {
            error();
        }

    }
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + ".hclip " + ChatFormatting.GRAY + "<"
                + "value" + ChatFormatting.GRAY + ">");
    }
}
