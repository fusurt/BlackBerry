package wtf.expensive.modules.impl.movement;

import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraModule;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.styles.StyleConfig;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "Auto Sprint", type = Type.MOVEMENT)

public class AutoSprintModule extends Module {

    private final EventListener<EventUpdate> onUpdate = e -> {
        if (!mc.player.isSneaking() && !mc.player.isCollidedHorizontally) {
            mc.player.setSprinting(MoveUtility.isMoving());
        }
    };

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.setSprinting(false);
        super.onDisable();
    }
}