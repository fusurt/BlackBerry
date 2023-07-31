package wtf.expensive.modules.impl.movement;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "DamageSpeed", type = Type.MOVEMENT)
public class DamageSpeedModule extends Module {
    public static double velocityXZ, velocityY;
    public static long lastVelocityTime;
    public static int ticks;
    public final EventListener<EventPacket> onPacketReceiveEvent = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (event.getPacket() instanceof SPacketEntityVelocity packet) {
                if (packet.getEntityID() == mc.player.getEntityId()
                        && System.currentTimeMillis() - lastVelocityTime > 1350) {
                    double vX = Math.abs(packet.getMotionX() / 8000d), vY = packet.getMotionY() / 8000d,
                            vZ = Math.abs(packet.getMotionZ() / 8000d);
                    if (vX + vZ > 0.3) {
                        velocityXZ = vX + vZ;
                        lastVelocityTime = System.currentTimeMillis();
                        velocityY = vY;
                    } else {
                        velocityXZ = 0;
                        velocityY = 0;
                    }
                }
            }
        }
    };

    private final EventListener<EventMotion> onMotion = e -> {
        if (System.currentTimeMillis() - lastVelocityTime < 1200) {
            ticks++;
            if ( MoveUtility.isMoving() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(0, 0.2, 0)).isEmpty()) {

                if (mc.player.onGround)
                    MoveUtility.setMotion(MathHelper.clamp(MoveUtility.getMotion() + 0.55, 0, 2));
                else
                    MoveUtility.setMotion(MoveUtility.getMotion() + 0.2f);
                StrafeModule.oldSpeed = MoveUtility.getMotion();
            }
        } else {
            ticks = 0;
        }
    };

}
