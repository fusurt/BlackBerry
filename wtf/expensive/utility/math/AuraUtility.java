package wtf.expensive.utility.math;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import wtf.expensive.utility.Utility;

import javax.vecmath.Vector2f;

public class AuraUtility implements Utility {

    public static float getAngle(EntityLivingBase entity) {
        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        return (float) Math.abs(MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) - mc.player.rotationYaw));
    }

    public static Vec3d getVecTarget(EntityLivingBase target, double distance) {
        Vec3d vec = target.getPositionVector().add(new Vec3d(0, MathHelper.clamp(target.getEyeHeight() * (mc.player.getDistanceToEntityEye(target) / (distance + target.width)), 0.2, mc.player.getEyeHeight()), 0));
        if (!isHitBoxVisible(vec)) {
            for (double i = target.width * 0.05; i <= target.width * 0.95; i += target.width * 0.9 / 8f) {
                for (double j = target.width * 0.05; j <= target.width * 0.95; j += target.width * 0.9 / 8f) {
                    for (double k = 0; k <= target.height; k += target.height / 8f) {
                        if (isHitBoxVisible(new Vec3d(i, k, j).add(target.getPositionVector().add(new Vec3d(-target.width / 2, 0, -target.width / 2))))) {
                            vec = new Vec3d(i, k, j).add(target.getPositionVector().add(new Vec3d(-target.width / 2, 0, -target.width / 2)));
                            break;
                        }
                    }
                }
            }
        }
        return vec;
    }

    public static boolean checkIfVisible(float angleX, float angleY, float hitboxSize, Entity target, double distance) {
        Vec3d playerPos = mc.player.getPositionEyes(1.0F);
        Vec3d lookDirection = mc.player.getVectorForRotation(angleY, angleX);
        Vec3d reach = playerPos.add(lookDirection.scale(distance));
        RayTraceResult result = mc.world.rayTraceBlocks(playerPos, reach, false, false, true);
        if (result == null) {
            return false;
        } else {
            return target.getEntityBoundingBox().expandXyz(hitboxSize).calculateIntercept(playerPos, reach) != null;
        }
    }

    public static Vector2f getVectorForCoord(Vector2f rot, Vec3d point) {
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        double x = point.x - client.posX;
        double y = point.y - client.getPositionEyes(1).y;
        double z = point.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
        float pitchDelta = (pitchToTarget - rot.y);
        return new Vector2f(yawDelta, pitchDelta);
    }

    public static Vector2f getRotationForCoord(Vec3d point) {
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        double x = point.x - client.posX;
        double y = point.y - client.getPositionEyes(1).y;
        double z = point.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        return new Vector2f(yawToTarget, pitchToTarget);
    }

    public static boolean isHitBoxVisible(Entity target, Vec3d vector, double dst) {
        return RaycastHelper.getPointedEntity(getRotationForCoord(vector), dst, 1, !entityBehindWall(),
                target) == target;
    }

    public static boolean isHitBoxVisible(Vec3d vec3d) {
        final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        return mc.world.rayTraceBlocks(eyesPos, vec3d, false, true, false) == null;
    }

    public static boolean entityBehindWall() {
        BlockPos pos = new BlockPos(mc.player.lastReportedPosX, mc.player.lastReportedPosY, mc.player.lastReportedPosZ);
        return mc.world.getBlockState(pos).getMaterial() == Material.AIR;
    }

}
