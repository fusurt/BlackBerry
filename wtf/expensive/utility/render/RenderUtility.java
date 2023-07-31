package wtf.expensive.utility.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import wtf.expensive.utility.Utility;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.shader.ShaderUtility;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class RenderUtility implements Utility {

    public static ShaderUtility roundedShader = new ShaderUtility("roundedRect");
    private static final ShaderUtility roundedGradientShader = new ShaderUtility("roundedRectGradient");
    public static ShaderUtility roundedOutlineShader = new ShaderUtility("roundRectOutline");
    public static ShaderUtility roundedTexturedShader = new ShaderUtility("roundedTexturedShader");

    public static void drawFadeString(final String s, float x, float y)
    {
        float updateX = x;

        for(int i = 0; i < s.length(); i++) {
            String str = String.valueOf(s.charAt(i));

            double colorOffset = (Math.abs(((System.currentTimeMillis()) / 20D)) / 50) + (50 / (mc.fontRendererObj.FONT_HEIGHT + i*14f + 50D));
            Color color = getGradientOffset1(new Color(0xffffff), new Color(0xababab), colorOffset, 255);

            Fonts.MONTSERRAT30.drawString(str, updateX, y, color.hashCode());
            updateX+= Fonts.MONTSERRAT30.getStringWidth(str);
        }
    }

    public static Color getGradientOffset1 (final Color color1, final Color color2, double offset, final int alpha){
        if(offset > 1){
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        final double inverse_percent = 1 - offset;
        final int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        final int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        final int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart, alpha);
    }

    public static void drawTriangle(int red, int green, int blue) {
        boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
        if (needBlend)
            GL11.glEnable(GL11.GL_BLEND);
        int alpha = 255;
        int red_2 = Math.max(red - 40, 0);
        int green_2 = Math.max(green - 40, 0);
        int blue_2 = Math.max(blue - 40, 0);
        float width = 6, height = 12;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        GL11.glVertex2d(0, 0 - height);
        GL11.glVertex2d(0 - width, 0);
        GL11.glVertex2d(0, 0 - 3);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(red_2 / 255f, green_2 / 255f, blue_2 / 255f, alpha / 255f);
        GL11.glVertex2d(0, 0 - height);
        GL11.glVertex2d(0, 0 - 3);
        GL11.glVertex2d(0 + width, 0);
        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (needBlend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        float f = (float) (startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (startColor & 0xFF) / 255.0f;
        float f4 = (float) (endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float) (endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float) (endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float) (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        glVertex2d(left, top);
        glVertex2d(right, top);
        GL11.glColor4f(f5, f6, f7, f4);
        glVertex2d(right, bottom);
        glVertex2d(left, bottom);


        GL11.glEnd();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();


    }

    public static void drawFace(int alpha, float d, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            float hurtPercent = getHurtPercent(target);
            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, alpha / 255f);
            Gui.drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawBlockBox(BlockPos blockPos, int red, float green, float blue, float alpha) {
        double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks;
        double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks;
        double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks;
        mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
        drawSelectionBoundingBox(mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos)
                .expandXyz(0.0020000000949949026D).offset(-x, -y, -z));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB axisalignedbb) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();

        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.draw();
        GL11.glPointSize(2);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.draw();
    }


    public static void renderItem(ItemStack itemStack, float x, float y) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();

        mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();

        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.enableAlpha();
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

    public static Frustum frustum = new Frustum();

    public static boolean isInViewFrustum(Entity entity) {
        return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void circleRound(float x, float y, float width, float height, float radius, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glBegin(7);


    }

    public static void drawBox(AxisAlignedBB bb) {
        GL11.glPushMatrix();
        mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vertexbuffer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);
        GL11.glPopMatrix();

    }

    public static void verticalGradient(float left, float top, float right, float bottom, final int startColor, final int endColor) {
        right += left;
        bottom += top;
        int f = (int) (startColor >> 24 & 255);
        int f1 = (int) (startColor >> 16 & 255);
        int f2 = (int) (startColor >> 8 & 255);
        int f3 = (int) (startColor & 255);
        int f4 = (int) (endColor >> 24 & 255);
        int f5 = (int) (endColor >> 16 & 255);
        int f6 = (int) (endColor >> 8 & 255);
        int f7 = (int) (endColor & 255);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();

        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();


    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color color, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);


        ColorUtility.setColor(color.getRGB(), alpha);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        GlStateManager.resetColor();
        GL11.glPopMatrix();

    }


    public static void drawRect(double left, double top, double right, double bottom, int color) {

        right += left;
        bottom += top;
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        drawRectNotWH(x, y, x + width, y + height, color);
        drawRectNotWH(x, y, x + width, y + lineSize, borderColor);
        drawRectNotWH(x, y, x + lineSize, y + height, borderColor);
        drawRectNotWH(x + width, y, x + width - lineSize, y + height, borderColor);
        drawRectNotWH(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public static void drawRectNotWH(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, int color) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        roundedShader.init();
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", 0);

        roundedShader.setUniformf("color", ColorUtility.getRed(color) / 255f, ColorUtility.getGreen(color) / 255f, ColorUtility.getBlue(color) / 255f, ColorUtility.getAlpha(color) / 255f);
        ShaderUtility.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, int color, boolean value) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedShader.init();
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", value ? 1 : 0);

        roundedShader.setUniformf("color", ColorUtility.getRed(color) / 255f, ColorUtility.getGreen(color) / 255f, ColorUtility.getBlue(color) / 255f, ColorUtility.getAlpha(color) / 255f);
        ShaderUtility.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public static void drawRoundCircle(float x, float y, float radius, int color) {
        drawRound(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, color);
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, int color, int outlineColor) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedOutlineShader.init();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("color", ColorUtility.getRed(color) / 255f, ColorUtility.getGreen(color) / 255f, ColorUtility.getBlue(color) / 255f, ColorUtility.getAlpha(color) / 255f);
        roundedOutlineShader.setUniformf("outlineColor", ColorUtility.getRed(color) / 255f, ColorUtility.getGreen(color) / 255f, ColorUtility.getBlue(color) / 255f, ColorUtility.getAlpha(color) / 255f);


        ShaderUtility.drawQuads(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness * 2), height + (4 + outlineThickness * 2));
        roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedGradientShader.init();
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        // Bottom Left
        roundedGradientShader.setUniformf("color1", ColorUtility.getRed(bottomLeft) / 255f, ColorUtility.getGreen(bottomLeft) / 255f, ColorUtility.getBlue(bottomLeft) / 255f, ColorUtility.getAlpha(bottomLeft) / 255f);
        //Top left
        roundedGradientShader.setUniformf("color2", ColorUtility.getRed(topLeft) / 255f, ColorUtility.getGreen(topLeft) / 255f, ColorUtility.getBlue(topLeft) / 255f, ColorUtility.getAlpha(topLeft) / 255f);
        //Bottom Right
        roundedGradientShader.setUniformf("color3", ColorUtility.getRed(bottomRight) / 255f, ColorUtility.getGreen(bottomRight) / 255f, ColorUtility.getBlue(bottomRight) / 255f, ColorUtility.getAlpha(bottomRight) / 255f);
        //Top Right
        roundedGradientShader.setUniformf("color4", ColorUtility.getRed(topRight) / 255f, ColorUtility.getGreen(topRight) / 255f, ColorUtility.getBlue(topRight) / 255f, ColorUtility.getAlpha(topRight) / 255f);
        ShaderUtility.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedGradientShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, int left, int right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }

    public static void horizontalGradient(double x1, double y1, double x2, double y2, int startColor, int endColor) {
        x2 += x1;
        y2 += y1;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x1, y1, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x1, y2, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x2, y2, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x2, y1, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawEntityBox(AxisAlignedBB player, float yaw, int color) {
        drawBoundingBox(player, yaw, color);
    }

    private static void drawBoundingBox(AxisAlignedBB bb, float yaw, int color) {
        double ix = -(Minecraft.getMinecraft().player.lastTickPosX + (Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.lastTickPosX) * Minecraft.getMinecraft().timer.renderPartialTicks);
        double iy = -(Minecraft.getMinecraft().player.lastTickPosY + (Minecraft.getMinecraft().player.posY - Minecraft.getMinecraft().player.lastTickPosY) * Minecraft.getMinecraft().timer.renderPartialTicks);
        double iz = -(Minecraft.getMinecraft().player.lastTickPosZ + (Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.lastTickPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks);
        GL11.glPushMatrix();
        GlStateManager.translate(ix, iy, iz);
//        get vec center bb
        GL11.glTranslated(bb.minX + (bb.maxX - bb.minX) / 2, bb.minY + (bb.maxY - bb.minY) / 2, bb.minZ + (bb.maxZ - bb.minZ) / 2);
        GL11.glRotated(yaw, 0.0D, 1.0D, 0.0D);
        GL11.glTranslated(-(bb.minX + (bb.maxX - bb.minX) / 2), -(bb.minY + (bb.maxY - bb.minY) / 2), -(bb.minZ + (bb.maxZ - bb.minZ) / 2));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F);
        GL11.glLineWidth(1F);
        GL11.glBegin(3);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glEnd();
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }
    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }
    public static void glHexColor(int hex, int alpha) {
        float red = (float)(hex >> 16 & 255) / 255.0F;
        float green = (float)(hex >> 8 & 255) / 255.0F;
        float blue = (float)(hex & 255) / 255.0F;
        GlStateManager.color(red, green, blue, (float)alpha / 255.0F);
    }
    public static void glColor(int hex) {
        float red = (float)(hex >> 16 & 255) / 255.0F;
        float green = (float)(hex >> 8 & 255) / 255.0F;
        float blue = (float)(hex & 255) / 255.0F;
        float alpha = hex >> 24 & 255;
        GlStateManager.color(red, green, blue, alpha);
    }
    public static void drawCircle(final float x, final float y, float start, float end, final float radius, final int color, final float linewidth) {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        RenderUtility.enableSmoothLine(linewidth);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glBegin(3);
        for (int i = (int)end; i >= (int)start; i -= 4) {
            RenderUtility.glColor(color);
            final float cos = (float)(Math.cos(Math.toRadians(i)) * radius * 1.);
            final float sin = (float)(Math.sin(Math.toRadians(i)) * radius * 1.);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        RenderUtility.disableSmoothLine();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

}
