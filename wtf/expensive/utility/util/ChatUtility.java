package wtf.expensive.utility.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import wtf.expensive.utility.Utility;

public class ChatUtility implements Utility {

    public static String chatPrefix = ChatFormatting.DARK_GRAY + "(" + ChatFormatting.LIGHT_PURPLE + "BLACKBERRY RElEASE"
            + ChatFormatting.DARK_GRAY + ") >> " + ChatFormatting.RESET;

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }

}
