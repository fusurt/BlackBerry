package wtf.expensive.modules.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.Outline;
import wtf.expensive.utility.shader.ShaderUtility;

@ModuleAnnotation(name = "ShaderESP", type = Type.RENDER)
public class ShaderESP extends Module {
    public Framebuffer framebuffer = new Framebuffer(1, 1, true);
    public ModeSetting colorMode = new ModeSetting("Color Mode", "Client", "Client", "Static");
    public BooleanSetting fill = new BooleanSetting("Fill", true);
    public ColorSetting color = new ColorSetting("Color", -1);
    public EventListener<EventDraw> onUpdate = e -> {
        if (e.type == EventDraw.RenderType.RENDER) {
            BloomUtil.update(framebuffer);
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(true);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GlStateManager.color(1, 1, 1, 1);
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player != mc.player) {
                    mc.getRenderManager().renderEntityStaticNoShadow(player, e.partialTicks, true);
                }
            }
            GlStateManager.resetColor();
            GL11.glPopMatrix();
            framebuffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(true);
        }
        if (e.type == EventDraw.RenderType.DISPLAY) {
            if (framebuffer != null) {
                GL11.glPushMatrix();
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.0f);
                GlStateManager.enableBlend();
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                ShaderUtility.bindTexture(framebuffer.framebufferTexture);
                BloomUtil.renderBlur(ShaderUtility::drawQuads, 10, 2, colorMode.is("Client") ? ColorUtility.getColorStyle(1) : color.get(), true);
                if (fill.get()) {
                    ShaderUtility.bindTexture(framebuffer.framebufferTexture);
                    Outline.renderBlur(ShaderUtility::drawQuads, colorMode.is("Client") ? ColorUtility.getColorStyle(1) : color.get());
                }
                GlStateManager.disableAlpha();
                GL11.glPopMatrix();
            }
        }
    };
}