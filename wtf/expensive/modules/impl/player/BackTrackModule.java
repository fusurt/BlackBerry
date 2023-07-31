package wtf.expensive.modules.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.pathfinder.Vec3;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.GaussianBlur;
import wtf.expensive.utility.shader.ShaderUtility;
import wtf.expensive.utility.util.ThreadUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleAnnotation(name = "BackTrack", type = Type.COMBAT)
public class BackTrackModule extends Module {

    public SliderSetting time = new SliderSetting("Time", 300, 50, 2000, 50);
    public List<Box> boxes = new CopyOnWriteArrayList<>();
    public TimerUtility timer = new TimerUtility();

    public long p1, p2, p3;
    private final EventListener<EventPacket> packetListener = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (event.getPacket() instanceof SPacketEntityTeleport packet) {
                if (mc.world == null || mc.player == null)
                    return;
                Entity entity = mc.world.getEntityByID(packet.getEntityId());
                if (entity == null || entity == mc.player)
                    return;
                boxes.add(new Box(entity, entity.getEntityBoundingBox(), new TimerUtility(), entity.rotationYaw));
            }
            if (event.getPacket() instanceof SPacketEntity packet) {
                if (mc.world == null || mc.player == null)
                    return;
                Entity entity = packet.getEntity(mc.world);
                if (entity == null || entity == mc.player)
                    return;
                boxes.add(new Box(entity, entity.getEntityBoundingBox(), new TimerUtility(), entity.rotationYaw));
            }
        }
        if (event.getPacketType() == EventPacket.PacketType.SEND) {
            if (event.getPacket() instanceof CPacketKeepAlive) {
                if (((CPacketKeepAlive) event.getPacket()).getKey() == p1) {
                    return;
                }
                event.setCancelled(true);
                ThreadUtil.run(() -> {
                    p1 = ((CPacketKeepAlive) event.getPacket()).getKey();
                    mc.getConnection().sendPacket(event.getPacket());
                }, (long) time.get());

            }
            if (event.getPacket() instanceof CPacketConfirmTransaction) {
                if (((CPacketConfirmTransaction) event.getPacket()).getUid() == p2) {
                    return;
                }
                if (((CPacketConfirmTransaction) event.getPacket()).getWindowId() == p3) {
                    return;
                }
                event.setCancelled(true);
                ThreadUtil.run(() -> {
                    p2 = ((CPacketConfirmTransaction) event.getPacket()).getUid();
                    p3 = ((CPacketConfirmTransaction) event.getPacket()).getWindowId();
                    mc.getConnection().sendPacket(event.getPacket());
                }, (long) time.get());
            }
        }
    };

    public Entity pointed(Entity e, float yaw, float pitch, float range) {
        Vec3d src = Minecraft.getMinecraft().player.getPositionEyes(mc.timer.renderPartialTicks);
        Vec3d vectorForRotation = mc.player.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);
        List<Box> copy = new CopyOnWriteArrayList<>(boxes);
        for (Box position : copy) {
            if (position == null) continue;
            if (position.player != e) continue;
            if (position.box.calculateIntercept(src, dest) != null) {
                return e;
            }
        }
        if (!state || (getNearest(e).distanceTo(e.getEntityBoundingBox().getCenter()) < 0.1f)) {
            if (e.getEntityBoundingBox().calculateIntercept(src, dest) != null) {
                return e;
            }
        }

        return null;
    }

    private final EventListener<EventDraw> renderListener = event -> {
        if (event.type == EventDraw.RenderType.RENDER) {
            List<Box> copy = new CopyOnWriteArrayList<>(boxes);
            copy.forEach(Box::draw);
        }
    };

    public Entity pointed() {

        Vec3d src = Minecraft.getMinecraft().player.getPositionEyes(mc.timer.renderPartialTicks);
        Vec3d vectorForRotation = mc.player.getLookVec();
        Vec3d dest = src.addVector(vectorForRotation.x * 3.1, vectorForRotation.y * 3.1, vectorForRotation.z * 3.1);
        List<Box> copy = new CopyOnWriteArrayList<>(boxes);
        for (Box position : copy) {
            if (position == null) continue;
            if (position.box.calculateIntercept(src, dest) != null) {
                return position.player;
            }
        }

        return null;
    }

    public Vec3d getNearest(Entity entity) {
        if (!this.state) {
            return entity.getEntityBoundingBox().getCenter();
        }

        if (entity.getEntityBoundingBox().getCenter().distanceTo(mc.player.getPositionEyes(1F)) < 3) {
            return entity.getEntityBoundingBox().getCenter();
        }

        return boxes.stream().filter(p -> p.player == entity).map(p -> p.box).min(Comparator.comparingDouble(p ->
                p.distanceTo(mc.player.getPositionEyes(1F))
        )).orElse(entity.getEntityBoundingBox()).getCenter();

    }

    public record Box(Entity player, AxisAlignedBB box, TimerUtility time, float yaw) {
        public void draw() {
            BackTrackModule module = Expensive.getInstance().getModuleManager().backTrackModule;
            if (time.hasTimeElapsed((long) module.time.get())) {
                module.boxes.remove(this);
                return;
            }
            long delay = System.currentTimeMillis() - time.getLastMS();
            int alpha = MathHelper.clamp((int) ((1 - (delay / module.time.get())) * 255), 0, 255);
            RenderUtility.drawEntityBox(box, yaw, ColorUtility.setAlpha(-1, alpha));
        }

    }
}