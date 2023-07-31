package wtf.expensive.modules.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "AutoRespawn", type = Type.PLAYER)


public class AutoRespawn extends Module {

    private final EventListener<EventUpdate> onUpd = e -> {
        if (mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    };
}
