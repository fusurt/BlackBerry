package wtf.expensive.modules.impl.movement;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.movement.MoveUtility;


@ModuleAnnotation(name = "Fly", type = Type.MOVEMENT)
public class FlightModule extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Matrix Float", "Matrix Jump", "Glide", "Void Jump");
    public SliderSetting motion = new SliderSetting("Motion", 1F, 0F, 8F, 0.1F);
    public SliderSetting motionY = new SliderSetting("Motion-Y", 1F, 0F, 8F, 0.1F);
    public BooleanSetting vanillaBypass = new BooleanSetting("Vanilla Bypass", true, () -> mode.is("Vanilla"));
    boolean damaged;

    public final EventListener<EventPacket> onPacket = e -> {
        if (e.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (mode.get().equals("Matrix Jump")) {
                if (e.getPacket() instanceof SPacketPlayerPosLook) {
                    mc.player.setPosition(((SPacketPlayerPosLook) e.getPacket()).x, ((SPacketPlayerPosLook) e.getPacket()).y, ((SPacketPlayerPosLook) e.getPacket()).z);
                    mc.player.connection.sendPacket(new CPacketConfirmTeleport(((SPacketPlayerPosLook) e.getPacket()).getTeleportId()));
                    mc.timer.timerSpeed = 1;
                    e.cancel();
                    toggle();
                }
            }
        }
    };


    public final EventListener<EventUpdate> onUpdate = e -> {
        setDisplayName(mode.get());
        switch (mode.get()) {
            case "Matrix Jump" -> {
                if (mc.player.onGround) mc.player.jump();
                else {
                    mc.timer.timerSpeed = 0.1f;
                    MoveUtility.setMotion(MathHelper.clamp(motion.get(), 0, 1.97f));
                    mc.player.motionY = motionY.get();
                }
            }
            case "Vanilla" -> {
                if (vanillaBypass.get()) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, true));
                    }
                    MoveUtility.setMotion(mc.player.ticksExisted % 2 == 0 ? motion.get() : 0);
                    mc.player.motionY = mc.player.ticksExisted % 2 == 0 ? (0.1 + (mc.gameSettings.keyBindJump.pressed ? motionY.get() : 0)) : -(0.1 + +(mc.gameSettings.keyBindSneak.pressed ? motionY.get() : 0));
                } else {
                    mc.player.motionY = 0.0;
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY += motionY.get() / 10;
                    }
                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.player.motionY -= motionY.get() / 10;
                    }
                    MoveUtility.setMotion(motion.get());
                }
            }
            case "Matrix Float" -> {
                if (mc.player.onGround) mc.player.motionY = 0.42;
                else {
                    mc.player.setVelocity(0, -0.003, 0);
                    MoveUtility.setMotion(motion.get());
                    StrafeModule.oldSpeed = motion.get();
                }
            }
            case "Glide" -> {
                if (damaged && mc.player.fallDistance > 0 && mc.player.ticksExisted % 4 == 0)
                    mc.player.motionY = -0.003;
                if (damaged) mc.timer.timerSpeed = 0.9f;
                if (mc.player.fallDistance >= 3) {
                    damaged = true;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.player.motionY = -0.003;
                    mc.player.fallDistance = 0;
                }
            }
            case "Void Jump" -> {
                if (mc.player.posY < 0 && mc.player.hurtTime != 0) mc.player.motionY = motionY.get();
            }
            case "Elytra" -> {
                if (!mc.player.onGround) {
                    if (!mc.player.wasFallFlying && mc.player.ticksExisted % 4 == 0) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        mc.player.jump();
                    } else if (mc.player.wasFallFlying) {
                        MoveUtility.setMotion((float) MoveUtility.getMotion());
                        mc.player.motionY += 0.02;
                    }
                }
            }
        }
    };


    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1;
        damaged = false;
        super.onDisable();
    }
}