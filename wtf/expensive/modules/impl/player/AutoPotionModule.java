package wtf.expensive.modules.impl.player;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraModule;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.utility.math.MathUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ModuleAnnotation(name = "AutoPotion", type = Type.PLAYER)
public class AutoPotionModule extends Module {
    public TimerUtility timerHelper = new TimerUtility();


    private final EventListener<EventUpdate> onUpdate = e -> {
        if (AuraModule.instance.target != null && mc.player.getCooledAttackStrength(1) > 0.5f) return;
        boolean check = !mc.player.onGround || mc.player.isOnLadder() || mc.player.isRiding()
                || mc.player.capabilities.isFlying || mc.player.isInWater() && !mc.player.onGround;
        boolean throwCheck = (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED))
                || (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH))
                || (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES));
        if (mc.player.ticksExisted > 15 && throwCheck && !check && timerHelper.hasTimeElapsed(700)) {
            float[] rotations = getRotationVector(getValidPosition());

            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));

            if (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED)) {
                sendPotion(Potions.SPEED);
            }
            if (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)) {
                sendPotion(Potions.STRENGTH);
            }
            if (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)) {
                sendPotion(Potions.FIRERES);
            }
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            timerHelper.reset();
        }

    };

    boolean send() {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -0.6, 0.0).expand(-0.29, 0.0, -0.29)).isEmpty();
    }

    public Vec3d getValidPosition() {
        final var outList = new ArrayList<AxisAlignedBB>();
        mc.world.func_191504_a(mc.player, mc.player.getEntityBoundingBox().expand(0, .1, 0), false, outList);
        return outList.stream().map(axis -> {
            final var center = axis.getCenter();
            final var x = MathHelper.clamp(mc.player.posX - center.x, -0.3, 0.3);
            final var y = MathHelper.clamp(mc.player.posY - center.y, -0.3, 0.3);
            final var z = MathHelper.clamp(mc.player.posZ - center.z, -0.3, 0.3);
            return center.addVector(x, y, z);
        }).min(Comparator.comparingDouble(this::getDistance)).orElse(null);
    }

    private float getDistance(Vec3d vector) {
        double x = mc.player.posX - vector.x;
        double y = mc.player.posY - vector.y;
        double z = mc.player.posZ - vector.z;
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static float[] getRotationVector(Vec3d vec) {
        Vec3d eyesPos = mc.player.getPositionEyes(1.0f);
        double diffX = vec != null ? vec.x - eyesPos.x : 0;
        double diffY = vec != null ? vec.y - (mc.player.posY + (double) mc.player.getEyeHeight() + 0.5) : 0;
        double diffZ = vec != null ? vec.z - eyesPos.z : 0;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathUtility.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathUtility.randomizeFloat(-2.0f, 2.0f);
        yaw = mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw);
        pitch = mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch);
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);

        return new float[]{yaw, pitch};
    }

    public void sendPotion(Potions potion) {
        int slot = getPotionSlot(potion);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static int getPotionSlot(Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (isStackPotion(mc.player.inventory.getStackInSlot(i), potion)) {
                return i;
            }
        }
        return -1;
    }
    // get all potions on hotbar

    public static boolean isPotionOnHotBar(Potions potions) {
        return getPotionSlot(potions) != -1;
    }

    // is stack
    public static boolean isStackPotion(ItemStack stack, Potions potion) {
        if (stack == null) return false;

        Item item = stack.getItem();

        if (item == Items.SPLASH_POTION) {
            int id = 0;

            switch (potion) {
                case STRENGTH: {
                    id = 5;
                    break;
                }
                case SPEED: {
                    id = 1;
                    break;
                }
                case FIRERES: {
                    id = 12;
                    break;
                }
                case HEAL: {
                    id = 6;
                    break;
                }
            }

            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Potion.getPotionById(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public enum Potions {
        STRENGTH, SPEED, FIRERES, HEAL
    }
}
