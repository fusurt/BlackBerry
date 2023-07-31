package wtf.expensive.modules.impl.combat;

import net.minecraft.init.Items;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventCalculateCooldown;
import wtf.expensive.event.impl.player.EventRightClick;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "GAppleCooldown", type = Type.COMBAT)
public class GAppleCooldown extends Module {

    public static long lastConsumeTime;

    private final EventListener<EventRightClick> onRightClick = e -> {

        long getLastTime = System.currentTimeMillis() - lastConsumeTime;

        if (getLastTime < 2300) {
            e.cancel();
        }
    };

    private final EventListener<EventCalculateCooldown> onCalculate = e -> {
        if (e.getStack() == Items.GOLDEN_APPLE) {
            long getLastTime = System.currentTimeMillis() - lastConsumeTime;
            if (getLastTime < 2600) {
                e.setCooldown(getLastTime / 2600f);
            }
        }
    };
}
