package wtf.expensive.modules.impl.player;

import net.minecraft.util.math.Vec3d;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventMove;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "NoClip", type = Type.PLAYER)
public class NoClip extends Module {


    private final EventListener<EventMove> onMove = move -> {
        if (!collisionPredict(move.to())) {
            if (move.isCollidedHorizontal())
                move.setIgnoreHorizontalCollision();
            if (move.motion().y > 0 || mc.player.isSneaking()) {
                move.setIgnoreVerticalCollision();
            }
            move.motion().y = Math.min(move.motion().y, 99999);
        }
    };

    public boolean collisionPredict(Vec3d to) {
        boolean prevCollision = mc.world
                .getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().contract(0.0625D)).isEmpty();
        Vec3d backUp = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
        mc.player.setPosition(to.x, to.y, to.z);
        boolean collision = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().contract(0.0625D))
                .isEmpty() && prevCollision;
        mc.player.setPosition(backUp.x, backUp.y, backUp.z);
        return collision;
    }

}
