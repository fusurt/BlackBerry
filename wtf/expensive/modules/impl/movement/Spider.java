package wtf.expensive.modules.impl.movement;

import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.ModeSetting;

@ModuleAnnotation(name = "Spider", type = Type.MOVEMENT)
public class Spider extends Module {
    TimerUtility timerUtils = new TimerUtility();
    private final ModeSetting mode = new ModeSetting("Mode", "Matrix 6.9.5", "Matrix 6.9.5");

    private final EventListener<EventMotion> onMotion = e -> {
        setDisplayName(mode.get());

        if (!mc.player.isCollidedHorizontally) return;

        if (timerUtils.hasTimeElapsed(200)) {
            e.setOnGround(true);
            mc.player.onGround = true;
            mc.player.isCollidedVertically = true;
            mc.player.isCollidedHorizontally = true;
            mc.player.isAirBorne = true;
            mc.player.jump();
            timerUtils.reset();
        }
    };
}
