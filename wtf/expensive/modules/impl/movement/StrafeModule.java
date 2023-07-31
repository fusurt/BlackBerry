package wtf.expensive.modules.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.*;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraModule;
import wtf.expensive.modules.impl.combat.TargetStrafeModule;
import wtf.expensive.modules.impl.player.NoWeb;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.movement.MoveUtility;

import java.util.Random;

@ModuleAnnotation(name = "Strafe", type = Type.MOVEMENT)
public class StrafeModule extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Matrix Hard");
    private float waterTicks = 0;
    public BooleanSetting elytra = new BooleanSetting("Elytra Boost", false);
    public SliderSetting setSpeed = new SliderSetting("Speed", 1.5F, 0.5F, 2.5F, 0.1F, () -> elytra.get());

    TimerUtility timerUtility = new TimerUtility();
    public static double oldSpeed, contextFriction;
    public static boolean needSwap, prevSprint, needSprintState;
    public static int counter, noSlowTicks;
    private int ticks;
    private final EventListener<EventMove> onMotion = e -> {
        if (mode.is("Matrix")) {
            if (mc.player.isInWater() || mc.player.isInLava()) {
                waterTicks = 10;
            } else {
                waterTicks--;
            }
            if (waterTicks > 0) return;
            if (MoveUtility.isMoving() && MoveUtility.getMotion() <= 0.289385188) {
                if (!e.toGround()) {
                    MoveUtility.setStrafe(MoveUtility.reason(false) || mc.player.isHandActive()
                            ? MoveUtility.getMotion() - 0.00001f : 0.245f - (new Random().nextFloat() * 0.000001f));
                }
            }
        }

    };
    private final EventListener<EventUpdate> eventListener = event -> {

    };
    private final EventListener<EventAction> onActionOneEvent = event -> {
        if (strafes() && mode.is("Matrix Hard")) {

            if (AuraModule.instance.hitDelay == 10) {
                return;
            }

            EventAction action = (EventAction) event;
            if (CPacketEntityAction.lastUpdatedSprint != needSprintState) {
                action.setSprintState(!CPacketEntityAction.lastUpdatedSprint);
            }
        }
    };
    private final EventListener<EventUpdate> onEventUpdate = event -> {
        setDisplayName(mode.get());
        if (!elytra.get()) return;
        int elytra = ElytraFly.getHotbarSlotOfItem();

        if (mc.player.isInWater() || mc.player.isInLava() || waterTicks > 0 || elytra == -1 || mc.player.isInWeb)
            return;
        if (mc.player.fallDistance != 0 && mc.player.fallDistance < 0.1 && mc.player.motionY < -0.1) {
            if (elytra != -2) {
                mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
            }
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));

            if (elytra != -2) {
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
            }
        }

    };
    private final EventListener<EventMove> onMove = event -> {
        int elytraSlot = ElytraFly.getHotbarSlotOfItem();


        if (elytra.get() && elytraSlot != -1) {

            if (MoveUtility.isMoving() && !mc.player.onGround && mc.player.fallDistance >= 0.15 && event.toGround()) {
                MoveUtility.setMotion(setSpeed.get());
                StrafeModule.oldSpeed = (setSpeed.get() / 1.06);
            }
        }

        if (mc.player.isInWater()) {
            waterTicks = 10;
        } else {
            waterTicks--;
        }

        if (!mode.is("Matrix Hard")) return;

        if (strafes()) {

            double forward = mc.player.movementInput.moveForward;
            double strafe = mc.player.movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;

            if (forward == 0.0 && strafe == 0.0) {

                oldSpeed = 0;
                event.motion().x = 0;
                event.motion().z = 0;

            } else {

                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }

                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }

                double speed = calculateSpeed(event);
                double cos = Math.cos(Math.toRadians(yaw + 90.0f)), sin = Math.sin(Math.toRadians(yaw + 90.0f));

                event.motion().x = forward * speed * cos + strafe * speed * sin;
                event.motion().z = forward * speed * sin - strafe * speed * cos;
            }
        } else {
            oldSpeed = 0;

        }
    };
    private final EventListener<EventPostMove> eventPostMoveEventListener = e -> {
        postMove(e.getHorizontalMove());
    };
    private final EventListener<EventPacket> onPacket = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                oldSpeed = 0;
            }
        }
    };
    private final EventListener<EventAction> onActionTwoEvent = event -> {
        if (mode.is("Matrix Hard")) {
            actionEvent(event);
        }
    };

    @Override
    public void onEnable() {
        oldSpeed = 0;
        super.onEnable();
    }

    public boolean strafes() {

        if (mc.player == null)
            return false;

        if (Expensive.getInstance().getModuleManager().get(ElytraFly.class).state && ElytraFly.lastStartFalling > 0)
            return false;
        if (Expensive.getInstance().getModuleManager().get(TargetStrafeModule.class).state && AuraModule.instance.target != null) {
            return false;
        }
        if (mc.player.isSneaking()) {
            return false;
        }
        if (mc.player.isInLava()) {
            return false;
        }

        if (mc.player.isInWater() || waterTicks > 0 && WaterSpeedModule.tick == 0) {
            return false;
        }
        if (NoWeb.move > 0) {
            return false;
        }
        if (mc.player.capabilities.isFlying) {
            return false;
        }
        return true;
    }

    public static double calculateSpeed(EventMove move) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean fromGround = mc.player.onGround;
        boolean toGround = move.toGround();
        boolean jump = move.motion().y > 0;
        float speedAttributes = getAIMoveSpeed(mc.player);
        final float frictionFactor = getFrictionFactor(mc.player, move);
        float n6 = mc.player.isPotionActive(MobEffects.JUMP_BOOST) && mc.player.isHandActive() ? 0.88f : (float) (oldSpeed > 0.32 && mc.player.isHandActive() ? 0.88 : 0.91F);
        if (fromGround) {
            n6 = frictionFactor;
        }
        float n7 = (float) (0.16277135908603668 / Math.pow(n6, 3.01));
        float n8;
        if (fromGround) {
            n8 = speedAttributes * n7;
            if (jump) {
                n8 += 0.2f;
            }
        } else {
            n8 = 0.0255f;
        }
        boolean noslow = false;
        double max2 = oldSpeed + n8;
        double max = 0.0;
        if (mc.player.isHandActive() && !jump) {
            double n10 = oldSpeed + n8 * 0.25;
            double motionY2 = move.motion().y;
            if (motionY2 != 0.0 && Math.abs(motionY2) < 0.08) {
                n10 += 0.055;
            }
            if (max2 > (max = Math.max(0.043, n10))) {
                noslow = true;
                ++noSlowTicks;
            } else {
                noSlowTicks = Math.max(noSlowTicks - 1, 0);
            }
        } else {
            noSlowTicks = 0;
        }
        if (noSlowTicks > 3) {
            max2 = max - 0.019;
        } else {
            max2 = Math.max(noslow ? 0 : 0.25, max2) - (counter++ % 2 == 0 ? 0.001 : 0.002);
        }
        contextFriction = n6;
        if (!toGround && !fromGround) {
            needSwap = true;
        } else {
            prevSprint = false;
        }
        if (!fromGround && !toGround) {
            needSprintState = !mc.player.serverSprintState;
        }
        if (toGround && fromGround) {
            needSprintState = false;
        }
        return max2;
    }

    public static void postMove(double horizontal) {
        oldSpeed = horizontal * contextFriction;
    }

    public static float getAIMoveSpeed(EntityPlayer contextPlayer) {
        boolean prevSprinting = contextPlayer.isSprinting();
        contextPlayer.setSprinting(false);
        float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
        contextPlayer.setSprinting(prevSprinting);
        return speed;
    }

    public static void actionEvent(EventAction eventAction) {
        if (needSwap) {
            eventAction.setSprintState(!Minecraft.getMinecraft().player.serverSprintState);
            needSwap = false;
        }
    }

    private static float getFrictionFactor(EntityPlayer contextPlayer, EventMove move) {
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos
                .retain(move.from().x, move.getAABBFrom().minY - 1.0D, move.from().z);
        return contextPlayer.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
    }

    private int getHotbarSlotOfItem() {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem() == Items.ELYTRA) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == Items.ELYTRA) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }
}
