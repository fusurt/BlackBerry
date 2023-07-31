package wtf.expensive.modules.impl.player;

import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "FastBreak", type = Type.PLAYER)
public class FastBreak extends Module {

    private final EventListener<EventUpdate> onUpdate = e -> {

        mc.playerController.blockHitDelay = 0;
        if (mc.playerController.curBlockDamageMP > 1f) {
            mc.playerController.curBlockDamageMP = 1F;
        }

    };

}
