package wtf.expensive.utility.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import wtf.expensive.utility.Utility;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_GREATER;

public class Outline implements Utility {

    public static ShaderUtility gaussianBloom = new ShaderUtility("expensive/shaders/outline.frag");

    public static Framebuffer framebuffer = ShaderUtility.createFrameBuffer(new Framebuffer(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight(), true));


    public static void update(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }
    }


    static Framebuffer framebuffer1 = ShaderUtility.createFrameBuffer(new Framebuffer(1, 1, false));
    public static void renderBlur(List<Runnable> run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.forEach(Runnable::run);
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, 10, 1, Color.BLACK.getRGB(), 2, true);

    }
    public static void renderBlur(Runnable run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, 10, 1, Color.BLACK.getRGB(), 2, true);
    }

    public static void renderBlur(Runnable run, int color) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, 0, 1, color, 0, false);
    }

    public static void renderBlur(Runnable run, int radius, int offset, int color, boolean fill, Framebuffer framebuffer) {
        update(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        run.run();
        framebuffer.unbindFramebuffer();

        renderBlur(framebuffer.framebufferTexture, radius, 1, color, offset, fill);
    }

    public static void renderBlur(int sourceTexture, int radius, int offset, int c, float des, boolean fill) {
        update(framebuffer);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= radius; i++) {
            weightBuffer.put(ShaderUtility.calculateGaussianValue(i, radius / 2f));
        }
        weightBuffer.rewind();

        setAlphaLimit(0.0F);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, 1, 0, weightBuffer, c, des, fill);
        ShaderUtility.bindTexture(sourceTexture);
        ShaderUtility.drawQuads();
        gaussianBloom.unload();
        framebuffer.unbindFramebuffer();


        mc.getFramebuffer().bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, 0, 1, weightBuffer, c, des, fill);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE16);
        ShaderUtility.bindTexture(sourceTexture);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        ShaderUtility.bindTexture(framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        gaussianBloom.unload();

        GlStateManager.alphaFunc(516, 0f);
        GlStateManager.enableAlpha();

        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights, int c, float des, boolean fill) {
        gaussianBloom.setUniformi("texture", 0);
        Color color = new Color(c);
        gaussianBloom.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        gaussianBloom.setUniformf("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
        gaussianBloom.setUniformf("direction", directionX, directionY);

    }
}