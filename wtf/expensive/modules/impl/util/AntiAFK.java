package wtf.expensive.modules.impl.util;

import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "AntiAFK", type = Type.UTIL)
public class AntiAFK extends Module {

    private final EventListener<EventUpdate> onUpdate = event -> {
        if (mc.player != null && mc.player.ticksExisted % 120 == 0) {
            mc.player.sendChatMessage("/yougayexpensivepena");
        }
    };
}
