package wtf.expensive.utility.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.event.impl.player.EventPreMove;
import wtf.expensive.modules.impl.movement.StrafeModule;
import wtf.expensive.utility.Utility;

public class MoveUtility implements Utility {
    public static double getDifferenceOf(final float num1,final float num2) {
        return Math.abs(num2-num1) > Math.abs(num1-num2) ? Math.abs(num1-num2) : Math.abs(num2-num1);
    }
    public static double getDifferenceOf(final double num1,final double num2) {
        return Math.abs(num2-num1) > Math.abs(num1-num2) ? Math.abs(num1-num2) : Math.abs(num2-num1);
    }
    public static double getDifferenceOf(final int num1,final int num2) {
        return Math.abs(num2-num1) > Math.abs(num1-num2) ? Math.abs(num1-num2) : Math.abs(num2-num1);
    }
    public static double getCuttingSpeed()
    {
        return Math.sqrt(Entity.Getmotionx * Entity.Getmotionx + Entity.Getmotionz * Entity.Getmotionz);
    }



    public static void setEventSpeed(EventPreMove move, float speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        move.setX((forward * speed * Math.cos(Math.toRadians(yaw + 90))
                + strafe * speed * Math.sin(Math.toRadians(yaw + 90))));
        move.setZ((forward * speed * Math.sin(Math.toRadians(yaw + 90))
                - strafe * speed * Math.cos(Math.toRadians(yaw + 90))));
    }
    public static void setMotion(double motion) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            StrafeModule.oldSpeed = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            mc.player.motionX = forward * motion * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * motion * Math.sin(Math.toRadians(yaw + 90.0f));
            mc.player.motionZ = forward * motion * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * motion * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static boolean reason(boolean water) {
        boolean critWater = water && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock()
                instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1,
                mc.player.posZ)).getBlock() instanceof BlockAir;
        return mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isOnLadder()
                || mc.player.isInWater() && !critWater || mc.player.isInWeb || mc.player.capabilities.isFlying;
    }
    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5),
                mc.player.posZ - 0.3);
        return !mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
    }
    public static boolean isMoving() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.movementInput.moveForward != 0.0;
    }
    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float strafeFactor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            strafeFactor = 1;
        if (mc.player.movementInput.moveForward < 0)
            strafeFactor = -1;

        if (strafeFactor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * strafeFactor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * strafeFactor;
        }

        if (strafeFactor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }

    public static void setStrafe(double motion) {
        if (!isMoving()) return;
        double radians = getDirection();
        mc.player.motionX = -Math.sin(radians) * motion;
        mc.player.motionZ = Math.cos(radians) * motion;
    }
    public static float getMotion() {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }
}
