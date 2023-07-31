package wtf.expensive.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventLiquidSolid;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "Jesus", type = Type.MOVEMENT)
public class Jesus extends Module {
    private int tick;


    private final EventListener<EventLiquidSolid> onLiquid = e -> {
        if (mc.player.isInWater()) return;
        if (mc.player.posY > e.getPos().getY() + (1 - 0.000009)) {
            e.setCollision(Block.FULL_BLOCK_AABB.expand(0, -0.000009, 0));
        }
    };
    private final EventListener<EventMotion> onMotion = e -> {
        if (mc.player.isInWater() || mc.player.isInLava()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.motionY = mc.world.handleMaterialAcceleration(mc.player.getEntityBoundingBox().expand(0, -1.1, 0).contract(.06), Material.WATER, mc.player) ? 0.19 : mc.player.motionY + .18;
        } else if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))
                .getBlock() instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX,
                mc.player.posY + 0.000009, mc.player.posZ)).getBlock() instanceof BlockAir) {
            mc.player.motionY = 0;
            e.setOnGround(false);

            if (mc.player.isCollidedHorizontally) {
                mc.player.motionY = 0.25;
                mc.player.motionX = mc.player.motionZ = 0;
                return;
            }

            if (tick == 1) {
                e.setY(e.getY() - 0.0311);
                MoveUtility.setMotion(1.1f);
                StrafeModule.oldSpeed = 1.1f;
                tick = 0;
            } else {
                tick = 1;
            }
        }
    };
}
