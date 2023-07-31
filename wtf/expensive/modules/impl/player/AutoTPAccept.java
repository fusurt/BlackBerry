package wtf.expensive.modules.impl.player;

import net.minecraft.network.play.server.SPacketChat;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.friendSystem.Friend;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.BooleanSetting;

@ModuleAnnotation(name = "AutoTPAccept", type = Type.PLAYER)
public class AutoTPAccept extends Module {
    TimerUtility timerHelper = new TimerUtility();
    private final BooleanSetting onlyfriends = new BooleanSetting("Only Friends", false);


    private final EventListener<EventPacket> onPacket = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (event.getPacket() instanceof SPacketChat message) {
                String m = message.getChatComponent().getUnformattedText();
                StringBuilder builder = new StringBuilder();
                char[] buffer = m.toCharArray();
                for (int i = 0; i < buffer.length; i++) {
                    if (buffer[i] == '§') {
                        i++;
                    } else {
                        builder.append(buffer[i]);
                    }
                }
                if (builder.toString().contains("телепортироваться")) {
                    if (onlyfriends.get()) {
                        for (Friend friends : Expensive.getInstance().friendManager.getFriends()) {
                            if (!builder.toString().contains(friends.getName()) || !timerHelper.hasTimeElapsed(500))
                                continue;
                            mc.player.sendChatMessage("/tpaccept");
                            timerHelper.reset();
                        }
                    } else if (timerHelper.hasTimeElapsed(300)) {
                        mc.player.sendChatMessage("/tpaccept");
                        timerHelper.reset();
                    }
                }
            }
        }
    };

}
