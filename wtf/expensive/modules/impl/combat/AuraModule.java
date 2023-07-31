package wtf.expensive.modules.impl.combat;


import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventInteractEntity;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.player.BackTrackModule;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.math.*;
import wtf.expensive.utility.movement.MoveUtility;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleAnnotation(name = "Aura", type = Type.COMBAT)

public class AuraModule extends Module {

    @Getter
    @Setter
    public EntityLivingBase target;

    public Vector2f rotation = new Vector2f();
    public ModeSetting rotateMode = new ModeSetting("Rotation Mode", "Advanced", "Advanced", "Vulcan");
    public MultiBoxSetting targetsSelection = new MultiBoxSetting("Target Selection", new String[]{"Players", "Naked Players", "NPC / Bots", "Mobs"});

    public SliderSetting range = new SliderSetting("Range", 3.0f, 2.5f, 8.0f, 0.05f);
    public SliderSetting rotateRange = new SliderSetting("Rotate Range", 0.5f, 0.f, 2.f, 0.05f);
    public BooleanSetting randomClicks = new BooleanSetting("Random Clicks", false, () -> rotateMode.is("Vulcan"));

    public BooleanSetting onlyCritical = new BooleanSetting("Only Critical", true);
    public BooleanSetting onlyWaterCritical = new BooleanSetting("Water Critical", true, () -> onlyCritical.get());
    public BooleanSetting onlyWeapon = new BooleanSetting("Only Weapon", false);
    public BooleanSetting targetESP = new BooleanSetting("Target ESP", false);
    public SliderSetting circleSpeed = new SliderSetting("Circle Speed", 2, 1, 5, 0.01f, () -> targetESP.get());
    public static double prevCircleStep, circleStep;

    private boolean rotatedBefore;
    public TimerUtility timer = new TimerUtility();
    public static AuraModule instance;
    public int hitDelay = 0;
    float Yaw = 0, Pitch = 0;
    float prevAdditionYaw;

    public AuraModule() {
        instance = this;
    }


    private final EventListener<EventInteractEntity> onInteract = e -> {
        if (target != null) {
            e.cancel();
        }
    };

    private final EventListener<EventUpdate> onUpdate = e -> {
        setDisplayName(rotateMode.get());
        boolean isTargetValid = target != null && isValid(target);
        if (!isTargetValid) target = findTarget();
        if (target == null) {
            rotation.x = mc.player.rotationYaw;
            rotation.y = mc.player.rotationPitch;
            return;
        }

        rotatedBefore = false;
        attack(target);

        if (!rotatedBefore) {
            rotation(target, false);
        }

    };
    public ItemStack lastData;
    public TimerUtility timerUtility = new TimerUtility();


    private final EventListener<EventMotion> onMotion = e -> {
        if (lastData != mc.player.getActiveItemStack()) {
            timerUtility.reset();
            lastData = mc.player.getActiveItemStack();
        }
        if (target != null) {
            e.setYaw(rotation.x);
            e.setPitch(rotation.y);
            mc.player.rotationYawHead = rotation.x;
            mc.player.renderYawOffset = rotation.x;


        }

    };
    private final EventListener<EventDraw> onDraw = event -> {
        if (event.type == EventDraw.RenderType.RENDER) {
            if (target != null && targetESP.get()) {

                prevCircleStep = circleStep;
                circleStep += circleSpeed.get() * AnimationMath.deltaTime();

                float eyeHeight = target.getEyeHeight();
                if (target.isSneaking()) eyeHeight -= 0.2f;

                double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.getRenderPartialTicks();
                double prevSinAnim = Math.abs(1 + Math.sin(cs - 0.5)) / 2;
                double sinAnim = Math.abs(1 + Math.sin(cs)) / 2;
                double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.getRenderPartialTicks()
                        - mc.getRenderManager().renderPosX;
                double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.getRenderPartialTicks()
                        - mc.getRenderManager().renderPosY + prevSinAnim * eyeHeight;
                double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.getRenderPartialTicks()
                        - mc.getRenderManager().renderPosZ;
                double nextY = target.lastTickPosY
                        + (target.posY - target.lastTickPosY) * mc.getRenderPartialTicks()
                        - mc.getRenderManager().renderPosY + sinAnim * eyeHeight;

                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i++) {
                    final Color color = ColorUtility.getColorStyleColor(i);
                    GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.6F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * target.width * 0.8, nextY,
                            z + Math.sin(Math.toRadians(i)) * target.width * 0.8);
                    GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.01F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * target.width * 0.8, y,
                            z + Math.sin(Math.toRadians(i)) * target.width * 0.8);
                }
                GL11.glEnd();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (int i = 0; i <= 360; i++) {
                    final Color color = ColorUtility.getColorStyleColor(i);
                    GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.8F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * target.width * 0.8, nextY,
                            z + Math.sin(Math.toRadians(i)) * target.width * 0.8);
                }
                GL11.glEnd();
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glShadeModel(GL11.GL_FLAT);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
                GlStateManager.resetColor();

            }
        }
    };

    private void attack(EntityLivingBase base) {
        if (whenFalling()) {

            rotation(base, true);

            if (DistanceUtility.calculateDistance(base, rotation.x, rotation.y,
                    range.get(), true) == base) {


                if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItem()
                        .getItemUseAction(mc.player.getActiveItemStack()) == EnumAction.BLOCK) {
                    mc.playerController.onStoppedUsingItem(mc.player);
                }
                timerUtility.reset();
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));

                mc.playerController.attackEntity(mc.player, base);
                mc.player.swingArm(EnumHand.MAIN_HAND);

                if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItem()
                        .getItemUseAction(mc.player.getActiveItemStack()) == EnumAction.BLOCK) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                }
                ShieldBreaker.breakShieldMethod(base, true);

            }

        }
    }


    public boolean whenFalling() {
        boolean critWater = onlyWaterCritical.get() && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() instanceof BlockAir;
        boolean reason = mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isOnLadder() || mc.player.isInWater() && !critWater || mc.player.isInWeb || mc.player.capabilities.isFlying;

        if (Expensive.getInstance().getModuleManager().backTrackModule.getNearest(target).distanceTo(mc.player.getPositionEyes(1)) > range.get()) return false;

        if (mc.player.getCooledAttackStrength(1.5f) < 0.92f) {
            return false;
        }

        if (onlyCritical.get() && !reason) {
            if (MoveUtility.isBlockAboveHead() && mc.player.onGround && mc.player.fallDistance > 0) {
                return true;
            }

            return (!mc.player.onGround && mc.player.fallDistance > (randomClicks.get() && rotateMode.currentMode.equals("Vulcan") ? MathUtility.randomizeFloat(0, 0.3f) : 0));
        }

        return true;
    }


    public final void rotation(EntityLivingBase base, boolean attack) {
        this.rotatedBefore = true;

        Vec3d bestHitbox = getVector(base, rotateRange.get() + range.get());

        if (bestHitbox == null) {
            bestHitbox = base.getPositionEyes(1);
        }

        if (Expensive.getInstance().getModuleManager().get(BackTrackModule.class).state
                && Expensive.getInstance().getModuleManager().backTrackModule.getNearest(base) != null) {
            bestHitbox = Expensive.getInstance().getModuleManager().backTrackModule.getNearest(base);
        }

        EntityPlayerSP client = Minecraft.getMinecraft().player;

        float sensitivity = 1.0001f;
        double x = bestHitbox.x - client.posX;
        double y = bestHitbox.y - client.getPositionEyes(1).y;
        double z = bestHitbox.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - rotation.x) / sensitivity;
        float pitchDelta = (pitchToTarget - rotation.y) / sensitivity;
        if (yawDelta > 180) {
            yawDelta = yawDelta - 180;
        }
        int yawDeltaAbs = (int) Math.abs(yawDelta);
        if (yawDeltaAbs < 180.0D) {
            switch (rotateMode.get()) {
                case "Advanced" -> {
                    float pitchDeltaAbs = Math.abs(pitchDelta);
                    float additionYaw = Math.min(Math.max(yawDeltaAbs, 1.0F), 80.F);
                    float additionPitch = Math.max(attack ? pitchDeltaAbs : 1, 2.0F);
                    if (Math.abs(additionYaw - this.prevAdditionYaw) <= 3.0f) {
                        additionYaw = GCDFixUtility.getSensitivity(this.prevAdditionYaw + 3.1f);
                    }

                    float newYaw = rotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
                    float newPitch = MathHelper.clamp(
                            rotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90, 90);
                    rotation.x = newYaw;
                    rotation.y = newPitch;
                    this.prevAdditionYaw = additionYaw;
                }
                case "Vulcan" -> {
                    float pitchDeltaAbs = Math.abs(pitchDelta);
                    float additionYaw = Math.min(Math.max(yawDeltaAbs, 1), 80);
                    float additionPitch = !AuraUtility.checkIfVisible(rotation.x, rotation.y, 0.06f, target, range.get() + rotateRange.get()) ? 10 : 1;
                    if (Math.abs(additionYaw - this.prevAdditionYaw) <= 3.0f) {
                        additionYaw = this.prevAdditionYaw + 3.1f;
                    }
                    float newYaw = rotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
                    float newPitch = MathHelper.clamp(
                            rotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90, 90);

                    rotation.x = newYaw;
                    rotation.y = newPitch;
                    this.prevAdditionYaw = additionYaw;
                }
            }
        } else {
            rotation.x = mc.player.rotationYaw;
            rotation.y = mc.player.rotationPitch;
        }
    }

    public EntityLivingBase findTarget() {
        List<EntityLivingBase> targets = new ArrayList<>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity)) {
                targets.add((EntityLivingBase) entity);
            }
        }

        targets.sort((e1, e2) -> {
            int dst1 = (int) (mc.player.getDistanceToEntity(e1) * 1000);
            int dst2 = (int) (mc.player.getDistanceToEntity(e2) * 1000);
            return dst1 - dst2;
        });

        return targets.isEmpty() ? null : targets.get(0);
    }

    public Vec3d getVector(Entity target, double rotateDistance) {
        if (target.getDistanceSqToEntity(target) >= 36) {
            return null;
        } else {
            Vec3d head = findHitboxCoord(Hitbox.HEAD, target);
            Vec3d chest = findHitboxCoord(Hitbox.CHEST, target);
            Vec3d legs = findHitboxCoord(Hitbox.LEGS, target);

            ArrayList<Vec3d> points = new ArrayList<>(Arrays.asList(head, chest));

            if (MoveUtility.isBlockAboveHead()) {
                points.add(legs);
            }

            points.removeIf(point -> !AuraUtility.isHitBoxVisible(target, point, rotateDistance));
            if (points.isEmpty()) {
                return null;
            } else {
                points.sort((d1, d2) -> {
                    Vector2f r1 = AuraUtility.getVectorForCoord(rotation, d1);
                    Vector2f r2 = AuraUtility.getVectorForCoord(rotation, d2);
                    float y1 = Math.abs(r1.y);
                    float y2 = Math.abs(r2.y);
                    return (int) ((y1 - y2) * 1000.0F);
                });
                return points.get(0);
            }
        }
    }


    public static Vec3d findHitboxCoord(Hitbox box, Entity target) {

        double yCoord = switch (box) {
            case HEAD -> target.getEyeHeight();
            case CHEST -> target.getEyeHeight() / 2;
            case LEGS -> 0.08;
        };
        return target.getPositionVector().addVector(0, yCoord, 0);
    }

    public boolean isValid(EntityLivingBase base) {

        if (base == mc.player || base.isDead || base.getHealth() <= 0)
            return false;
        if ((Expensive.getInstance().getModuleManager().
                backTrackModule.getNearest(base).distanceTo(mc.player.getPositionEyes(1)) > range.get() + rotateRange.get()))
            return false;
        if (base.getPositionVector().distanceTo(mc.player.getPositionEyes(1)) > 6 + rotateRange.get())
            return false;
        if (base instanceof EntityPlayer && base.getTotalArmorValue() == 0 && !targetsSelection.get(1))
            return false;
        if ((base instanceof EntityPlayer || base.isInvisible()) && !targetsSelection.get(0))
            return false;
        if (this.onlyWeapon.get() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword
                || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe)) {
            return false;
        }
        if (base.getName().equals(mc.player.getName())) return false;
        if (Expensive.getInstance().friendManager.isFriend(base.getName()))
            return false;
        if ((base instanceof EntityMob || base instanceof EntityAnimal || base instanceof EntityGolem
                || base instanceof EntitySlime || base instanceof EntitySquid || base instanceof EntityVillager) && !targetsSelection.get(3))
            return false;
        if (base instanceof EntityPlayer && ((EntityPlayer) base).isBot)
            return false;

        if (base instanceof EntityArmorStand)
            return false;

        return true;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        Yaw = mc.player.rotationYaw;
        Pitch = mc.player.rotationPitch;
        rotation.x = mc.player.rotationYaw;
        rotation.y = mc.player.rotationPitch;
        target = null;
        super.onDisable();
    }

    public float[] Rotation(Entity e) {
        AxisAlignedBB aabb = e.getRenderBoundingBox();
        double eX = aabb.minX + ((aabb.maxX - aabb.minX) / 2);
        double eY = aabb.minY;
        double eZ = aabb.minZ + ((aabb.maxZ - aabb.minZ) / 2);
        double x = eX - mc.player.posX;
        double z = eZ - mc.player.posZ;
        EntityLivingBase ent = (EntityLivingBase) e;
        double y = eY + ent.getEyeHeight() * 0.666666666f;
        double dy = y - (mc.player.posY + (double) mc.player.getEyeHeight());
        double dstxz = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI - 92.0);
        float pitch = (float) (Math.toDegrees(-Math.atan2(dy, dstxz)));
        yaw = Yaw + GCDFixUtility.getSensitivity(MathHelper.wrapDegrees(yaw - Yaw));
        pitch = Pitch + GCDFixUtility.getSensitivity(MathHelper.wrapDegrees(pitch - Pitch));
        pitch = MathHelper.clamp(pitch, -88.5f, 89.9f);
        return new float[]{yaw, pitch};
    }

    public float[] getNeededFacing2(Entity target, boolean randomizer) {
        float yaw = Rotation(target)[0];
        float pitch = Rotation(target)[1];
        int speed = mc.player.ticksExisted % 2 == 0 ? 20 : 1;
        if (MoveUtility.isMoving() || MoveUtility.getDifferenceOf(Yaw, yaw) > 10) {
            Yaw = MathUtility.lerp(Yaw, yaw, 0.05f * speed);
            Pitch = MathUtility.lerp(Pitch, pitch, 0.05f * speed);
            Pitch = MathUtility.clamp(Pitch, -90, 90);
            Yaw = Yaw + GCDFixUtility.getSensitivity(MathHelper.wrapDegrees(Rotation(target)[0] - Yaw));
            Pitch = Pitch + GCDFixUtility.getSensitivity(MathHelper.wrapDegrees(Rotation(target)[1] - Pitch));
        }
        return new float[]{Yaw, Pitch};
    }

    public enum Hitbox {
        HEAD, CHEST, LEGS
    }
}
