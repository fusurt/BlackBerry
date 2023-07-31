package wtf.expensive.modules.impl.movement;

import net.minecraft.util.math.MathHelper;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "Timer", type = Type.MOVEMENT)
public class TimerModule extends Module {
    public static SliderSetting timerAmount = new SliderSetting("Timer Value", 2, 1, 10, 0.01f);

    public static BooleanSetting smart = new BooleanSetting("Smart", true);

    public static long lastUpdateTime;
    public static double value;
    public static float animWidth;

    public static int getMin() {
        return -(15);
    }

    private final EventListener<EventUpdate> onUpdateEvent = e -> {
        if (!smart.get() || canEnableTimer(timerAmount.get() + 0.2f)) {

            mc.timer.timerSpeed = Math.max(timerAmount.get() + (mc.player.ticksExisted % 2 == 0 ? -0.2f : 0.2f), 0.1f);
        } else {
            mc.timer.timerSpeed = 1;
            toggle();
        }
    };


    public static double getProgress() {
        return (10 - value) / (Math.abs(getMin()) + 10);
    }

    public static boolean canEnableTimer(float speed) {
        double predictVl = (50.0 - (double) 50 / speed) / 50.0;
        return predictVl + value < 10 - timerAmount.get();
    }

    public boolean canEnableTimerIgnoreSettings(float speed) {
        double predictVl = (50.0 - (double) 50 / speed) / 50.0;
        return predictVl + value < 10;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }

    public static void updateTimer() {
        if (!smart.get()) return;
        long now = System.currentTimeMillis();
        long timeElapsed = now - lastUpdateTime;
        lastUpdateTime = now;
        value += (50.0 - (double) timeElapsed) / 50.0;
        value -= 0.001;
        value = MathHelper.clamp(value, getMin(), 25.0);
    }

}
