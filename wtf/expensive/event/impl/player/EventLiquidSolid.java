package wtf.expensive.event.impl.player;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.event.Event;

import static net.minecraft.block.Block.NULL_AABB;

    public class EventLiquidSolid extends Event  {
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;
    private AxisAlignedBB collision;

    public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
        this.collision = NULL_AABB;
    }

    public BlockLiquid getBlock() {
        return blockLiquid;
    }

    public BlockPos getPos() {
        return pos;
    }

    public AxisAlignedBB getCollision() {
        return collision;
    }

    public void setCollision(AxisAlignedBB collision) {
        this.collision = collision;
    }
}