package wtf.expensive.modules.impl.movement;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemElytra;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventMove;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "Speed", type = Type.MOVEMENT)
public class SpeedModule extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Matrix Timer", "Sunrise Damage");

    public int jump;

    private final EventListener<EventMove> onMove = move -> {
        if (mode.is("Matrix")) {

            if (!mc.player.onGround && move.toGround()) {
                double speed = 2;
                move.motion().x *= speed;
                move.motion().z *= speed;
                mc.player.motionX *= speed;
                mc.player.motionZ *= speed;
                StrafeModule.oldSpeed *= speed;
            }
        }

    };

    private final EventListener<EventUpdate> onUpdateEvent = e -> {
        setDisplayName(mode.get());
        if (mode.is("Matrix")) {
            if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
                mc.player.jump();
            }
        }
        if (mode.is("Matrix Elytra")) {

        }
        if (mode.is("Matrix Timer")) {
            float timerValue = mc.player.fallDistance <= 0.25f ? 2.2f : (float) (mc.player.fallDistance != Math.ceil(mc.player.fallDistance) ? 0.4f : 1f);
            if (TimerModule.canEnableTimer(timerValue + 0.2f) && MoveUtility.isMoving()) {
                mc.timer.timerSpeed = timerValue;
                if (mc.player.onGround) {
                    mc.player.jump();
                }
            } else {
                mc.timer.timerSpeed = 1.0f;
            }
        }
        if (mode.is("Sunrise Damage")) {
            StrafeModule.oldSpeed = MoveUtility.getMotion();

            if (mc.player.onGround)
                MoveUtility.setMotion(MathHelper.clamp(MoveUtility.getMotion() + 0.4, 0, 2));
            else
                MoveUtility.setMotion(MoveUtility.getMotion());
        }
    };


    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
