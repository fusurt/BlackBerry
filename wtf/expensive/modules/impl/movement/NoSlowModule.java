package wtf.expensive.modules.impl.movement;

import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "NoSlow", type = Type.MOVEMENT)
public class NoSlowModule extends Module {

    private final EventListener<EventUpdate> onUpdate = e -> {
        if (mc.player.isHandActive()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (mc.player.fallDistance >= 0.5) {
                    mc.player.motionX *= 0.95;
                    mc.player.motionZ *= 0.95;
                }
            } else {
                if (mc.player.ticksExisted % 2 == 0) {
                    mc.player.motionX *= 0.47;
                    mc.player.motionZ *= 0.47;
                }
            }
        }
    };
}
