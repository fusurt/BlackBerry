package wtf.expensive.modules.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.render.Notifications;
import wtf.expensive.utility.util.ChatUtility;

@ModuleAnnotation(name = "DeathCoords", type = Type.PLAYER)
public class DeathCoords extends Module {


    private final EventListener<EventUpdate> onUpdate = event -> {
        if (mc.player.getHealth() < 1.0f && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.deathTime < 1) {
                Notifications.notify("Death Debug", "X: " + x + " Y: " + y + " Z: " + z, Notifications.Notify.NotifyType.INFORMATION,10);
                ChatUtility.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    };
}
