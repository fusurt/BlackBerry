package wtf.expensive.utility.pathfinder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.Vec3d;

/**
 * @author Auth
 * @since 28/06/2022
 */

@Getter@Setter
@AllArgsConstructor
public class Vec3 {

    public double x;
    public double y;
    public double z;

    public Vec3 addVector(final double x, final double y2, final double z) {
        return new Vec3(this.x + x, this.y + y2, this.z + z);
    }

    public Vec3 floor() {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(final Vec3 vector) {
        return Math.pow(vector.x - this.x, 2.0) + Math.pow(vector.y - this.y, 2.0) + Math.pow(vector.z - this.z, 2.0);
    }

    public Vec3 add(final Vec3 vector) {
        return this.addVector(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vec3d mc() {
        return new Vec3d(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}