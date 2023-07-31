package wtf.expensive.modules.impl.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;


import java.awt.*;

@ModuleAnnotation(name = "Tracers", type = Type.RENDER)
public class TracersModule extends Module {

    public ColorSetting color = new ColorSetting("Color", new Color(255, 255, 255).getRGB());
    public ColorSetting friendColor = new ColorSetting("Friend Color", Color.GREEN.getRGB());

    public SliderSetting width = new SliderSetting("Width", 1, 1, 10, 0.1f);

    public SliderSetting alpha = new SliderSetting("Alpha", 255, 0, 255, 1);

    public BooleanSetting ignoreNaked = new BooleanSetting("Ignore Naked", true);
    private final EventListener<EventDraw> onDraw = e -> {
        if (e.type == EventDraw.RenderType.RENDER) {

            GL11.glPushMatrix();
            boolean old = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = false;
            mc.entityRenderer.setupCameraTransform(e.partialTicks, 2);
            mc.gameSettings.viewBobbing = old;
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(width.get());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float)
                    -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float)
                    -Math.toRadians(mc.player.rotationYaw));
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

            for (Entity entity : mc.world.loadedEntityList) {
                if (entity != mc.player && entity instanceof EntityPlayer) {

                    if (ignoreNaked.get() && ((EntityPlayer) entity).getTotalArmorValue() == 0) {
                        continue;
                    }

                        Color c = Expensive.getInstance().friendManager.isFriend(entity.getName()) ? friendColor.getColor() : color.getColor();
                    float red = c.getRed() / 255F;
                    float green = c.getGreen() / 255F;
                    float blue = c.getBlue() / 255F;
                    float alpha = ((int) this.alpha.get()) / 255F;

                    double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks() - mc.getRenderManager().renderPosX;
                    double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks() - mc.getRenderManager().renderPosY;
                    double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks() - mc.getRenderManager().renderPosZ;

                    GlStateManager.color(red, green, blue, alpha);

                    bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
                    bufferBuilder.pos(vec.x, vec.y + mc.player.getEyeHeight(), vec.z).endVertex();
                    bufferBuilder.pos(x, y, z).endVertex();
                    Tessellator.getInstance().draw();
                }
            }
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            mc.entityRenderer.setupCameraTransform(e.partialTicks, 0);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    };

}
