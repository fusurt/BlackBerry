package wtf.expensive.utility.pathfinder;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Path {
    public Vec3d start;
    public Vec3d end;
    public List<Vec3d> path = new ArrayList<>();

    public Path(Vec3d from, Vec3d to) {
        this.start = from;
        this.end = to;
    }

    public Vec3d getStart() {
        return start;
    }

    public Vec3d getEnd() {
        return end;
    }

    public List<Vec3d> getPath() {
        return path;
    }

    public void calculatePath(float step) {
        for (float i = 0; i < start.distanceTo(end); i += step) {
            float x = (float) (start.x + i * (end.x - start.x) / start.distanceTo(end));
            float y = (float) start.y;
            float z = (float) (start.z + i * (end.z - start.z) / start.distanceTo(end));

            if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAir) {
                path.add(new Vec3d(x, y, z));
            } else {
                path.add(new Vec3d(x, y + 1, z));
            }
        }
    }


}
