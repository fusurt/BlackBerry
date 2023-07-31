package wtf.expensive.utility.shader;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static wtf.expensive.utility.shader.StencilUtil.mc;

public class SmartStencil {
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    public static void update(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }
    }
    public static void renderStart(Runnable clamp) {
        update(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        clamp.run();
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);

        if (framebuffer != null) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            mc.getFramebuffer().bindFramebuffer(true);
            ShaderUtility.bindTexture(framebuffer.framebufferTexture);
            StencilUtil.initStencilToWrite();
            clamp.run();
            StencilUtil.readStencilBuffer(1);
        }

    }

    public static void renderEnd() {
        if (framebuffer != null) {


            StencilUtil.uninitStencilBuffer();
            mc.getFramebuffer().bindFramebuffer(false);

            GlStateManager.disableAlpha();
            GL11.glPopMatrix();

        }
    }

}
