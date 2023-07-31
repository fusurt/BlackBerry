package wtf.expensive.modules.impl.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventLivingUpdate;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "FreeCam", type = Type.PLAYER)
public class FreeCam extends Module {

    private final SliderSetting speed = new SliderSetting("Flight Speed", 1.0f, 0.1f, 5.0f, 0.05f);
    private final SliderSetting motionY = new SliderSetting("Motion Y", 0.5f, 0.1f, 1, 0.05f);
    private double oldPosX = 0, oldPosY = 0, oldPosZ = 0;


    private final EventListener<EventLivingUpdate> onLiving = e -> {
        mc.player.noClip = true;
        mc.player.onGround = false;
        MoveUtility.setMotion(speed.get());

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = motionY.get();
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -motionY.get();
        }

        mc.player.capabilities.isFlying = true;
    };

    private final EventListener<EventMotion> onMotion = e -> {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null
                && (!mc.getCurrentServerData().serverIP.contains("sunrise"))) {
            if (mc.player.ticksExisted % 10 == 0) {
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
            }
        }
        e.cancel();
    };

    private final EventListener<EventDraw> onDraw = e -> {
        if (e.type == EventDraw.RenderType.DISPLAY) {
            ScaledResolution resolution = e.sr;

            String posY = "" + (int) -(oldPosY - mc.player.posY);
            String plusOrMinusY = !posY.contains("-") && !posY.equals("0") ? "+" : "";
            String clipValue = ".vclip " + plusOrMinusY + posY;

            Expensive.getInstance().getScaleMath().pushScale();

            Fonts.SEMI_BOLD_16.drawCenteredString(clipValue, resolution.getScaledWidth() / 2F,
                    resolution.getScaledHeight() / 2F + 10, -1);

            Expensive.getInstance().getScaleMath().popScale();
        }
    };

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null) return;
        oldPosX = mc.player.posX;
        oldPosY = mc.player.posY;
        oldPosZ = mc.player.posZ;
        EntityOtherPlayerMP player = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        player.copyLocationAndAnglesFrom(mc.player);
        player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(68812, player);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player == null) return;
        if (!mc.player.isCreative()) mc.player.capabilities.isFlying = false;
        mc.world.removeEntityFromWorld(68812);
        mc.player.motionZ = 0;
        mc.player.motionX = 0;
        mc.player.setPositionAndRotation(oldPosX, oldPosY, oldPosZ, mc.player.rotationYaw, mc.player.rotationPitch);
    }
}
