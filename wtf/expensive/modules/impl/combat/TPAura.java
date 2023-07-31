package wtf.expensive.modules.impl.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.pathfinder.MainPathFinder;
import wtf.expensive.utility.pathfinder.Path;
import wtf.expensive.utility.pathfinder.Vec3;
import wtf.expensive.utility.util.ChatUtility;
import wtf.expensive.utility.util.SoundUtility;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ModuleAnnotation(name = "TPAura", type = Type.COMBAT)
public class TPAura extends Module {

    public Path path;
    public SliderSetting range = new SliderSetting("Range", 15, 10, 100, 1);
    public SliderSetting step = new SliderSetting("Step", 5, 2, 50, 1);
    public BooleanSetting crits = new BooleanSetting("Criticals", true);
    public BooleanSetting render = new BooleanSetting("Visual", true);
    public BooleanSetting swap = new BooleanSetting("Swap", false);
    public BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public static EntityLivingBase entity;

    public EventListener<EventMotion> e = e -> {
        setDisplayName(String.valueOf(range.get()));
        entity = (EntityLivingBase) getNearestEntity();

        if (entity == null) {
            path = null;
            return;
        }
        path = new Path(mc.player.getPositionVector(), entity.getPositionVector());
        path.calculatePath(step.get());
        if (rotate.get()) {
            Vec2f rotations = getRotationToEntity(entity);
            e.setYaw(rotations.x);
            e.setPitch(rotations.y);
        }
        if (mc.player.getCooledAttackStrength(1) == 1) {
            if (crits.get())
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ, false));
            for (Vec3d vec : path.getPath()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(vec.x, vec.y, vec.z, mc.player.rotationYaw, mc.player.rotationPitch, false));
            }
            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            mc.player.resetCooldown();
            if (swap.get())
                mc.player.swingArm(EnumHand.MAIN_HAND);
            Collections.reverse(path.getPath());
            for (Vec3d vec : path.getPath()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(vec.x, vec.y, vec.z, mc.player.rotationYaw, mc.player.rotationPitch, false));
            }
        }


    };

    @Override
    public void onDisable() {
        super.onDisable();
        entity = null;
    }

    public Vec2f getRotationToEntity(Entity entity) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3d entityPos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        double diffX = entityPos.x - eyesPos.x;
        double diffY = entityPos.y - eyesPos.y;
        double diffZ = entityPos.z - eyesPos.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
        return new Vec2f(yaw, pitch);
    }

    public EventListener<EventDraw> draw = e -> {
        if (e.type == EventDraw.RenderType.RENDER) {
            if (path == null || !render.get()) return;
            for (Vec3d entity : path.getPath()) {

                if (mc.player.getPositionVector().distanceTo(entity) < 2) continue;

                double x = (entity.x) - mc.getRenderManager().renderPosX;
                double y = (entity.y) - mc.getRenderManager().renderPosY;
                double z = (entity.z) - mc.getRenderManager().renderPosZ;
                GL11.glPushMatrix();
                mc.entityRenderer.setupCameraTransform(e.partialTicks, 2);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);
                GlStateManager.color(1, 1, 1, 0.05f);
                Render<Entity> render = null;
                try {
                    render = mc.getRenderManager().getEntityRenderObject(mc.player);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (render != null) {
                    (RenderLivingBase.class.cast(render)).doRenderT((EntityLivingBase) mc.player, x, y, z, 1, mc.getRenderPartialTicks());
                }
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);


                mc.entityRenderer.setupCameraTransform(e.partialTicks, 0);
                GlStateManager.resetColor();
                GL11.glPopMatrix();
            }
        }
    };


    public Entity getNearestEntity() {
        Entity nearest = null;
        double distance = range.get();

        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity == mc.player) continue;

            if (entity.isBot) {
                continue;
            }

            double d = mc.player.getDistanceToEntity(entity);

            if (nearest == null && d < distance) {
                nearest = entity;
                distance = d;
            }
        }

        return nearest;
    }


}
