package wtf.expensive.modules.impl.movement;

import net.minecraft.util.math.AxisAlignedBB;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "NoJumpDelay", type = Type.MOVEMENT)
public class NoJumpDelay extends Module {

    private final EventListener<EventUpdate> onUpdate = e -> {
        if (mc.player == null || !mc.player.isEntityAlive() || mc.world == null) return;
        final AxisAlignedBB aabb = mc.player.getEntityBoundingBox();
        if (aabb == null) return;
        boolean hasUpCollide = !mc.world.getCollisionBoxes(mc.player,aabb.offsetMXMN(0,0,0,0,2 - mc.player.height + .3F,0)).isEmpty();
        boolean hasDownCollide = !mc.world.getCollisionBoxes(mc.player,aabb.offsetMXMN(0,-.31D + mc.player.motionY,0,0,0,0)).isEmpty();
        if (hasUpCollide && hasDownCollide)
        mc.player.jumpTicks = 0;
    };
}