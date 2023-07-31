package wtf.expensive.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.movement.MoveUtility;

import java.util.Arrays;

@ModuleAnnotation(name = "AirJump", type = Type.MOVEMENT)
public class AirJump extends Module {


    private final EventListener<EventMotion> onUpdate = e -> {

        if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(0.5, 0.0, 0.5)
                .offset(0.0, -1, 0.0)).isEmpty() && mc.player.ticksExisted % 2 == 0) {
            mc.player.jumpTicks = 0;
            mc.player.fallDistance = 0.0f;
           e.setOnGround(true);
            mc.player.onGround = true;
        }
    };

}
