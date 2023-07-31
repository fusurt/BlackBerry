package wtf.expensive.ui.csgo.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class ColorElement extends Element {

    public ColorSetting colorSetting;
    public ModuleElement moduleElement;

    public ColorElement(ModuleElement moduleElement, ColorSetting colorSetting) {

        this.colorSetting = colorSetting;
        float[] hsb = Color.RGBtoHSB(colorSetting.getColor().getRed(), colorSetting.getColor().getGreen(), colorSetting.getColor().getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        this.moduleElement = moduleElement;


    }

    float hue;
    float saturation;
    float brightness;
    boolean draggingHue = false;

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        float[] hsb = Color.RGBtoHSB(colorSetting.getColor().getRed(), colorSetting.getColor().getGreen(), colorSetting.getColor().getBlue(), null);

        height = 70;

        Fonts.nunito12.drawString(colorSetting.getName(), x + 5, y + 2.5, Color.WHITE.getRGB());

        float y = this.y + 10;

        RenderUtility.drawGradientRound(x + 4, y + 2, width - 8, 60 - 10, 2, ColorUtility.rgba(255, 255, 255, 255), Color.BLACK.getRGB(), new Color(Color.HSBtoRGB(hue, 1, 1)).getRGB(), Color.BLACK.getRGB());
        for (int i = 0; i <= width - 8; i++) {
            RenderUtility.drawRect(x + 4 + i, y + 60 - 5, 1f, 3, Color.HSBtoRGB(i / (width - 8f), 1, 1));


        }

        if (draggingHue) {
            hue = (mouseX - (x + 4)) / (width - 8f);
            colorSetting.setColorValue(Color.HSBtoRGB(hue, saturation, brightness));
        }

        hue = MathHelper.clamp(hue, 0, 1);

        RenderUtility.drawRect(x + 4 + hue * (width - 8), y + 60 - 5, 1, 3, ColorUtility.rgba(255, 255, 255, 255));

//        get color from mouse

        if (HoveringMath.isHovering(x + 4, y + 2, width - 8, 60 - 10, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            float saturation = (mouseY - (y + 2)) / (60 - 10f);
            saturation = MathHelper.clamp(saturation, 0, 1);
            float brightness = (mouseX - (x + 4)) / (width - 8f);
            brightness = MathHelper.clamp(brightness, 0, 1);
            this.brightness = 1 - saturation;
            this.saturation = brightness;
            colorSetting.setColorValue(Color.HSBtoRGB(hue, this.saturation, this.brightness));
        }




//        draw circle
        RenderUtility.drawRoundCircle(x + 4 + saturation * (width - 8), y + 2 + (1 - brightness) * (60 - 10), 3, ColorUtility.rgba(21, 21, 21, 255));
        RenderUtility.drawRoundCircle(x + 4 + saturation * (width - 8), y + 2 + (1 -brightness) * (60 - 10), 2, colorSetting.get());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (HoveringMath.isHovering(x + 4, y + height - 6, width - 8, 5, mouseX, mouseY)) {
            draggingHue = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggingHue = false;
    }

    public void drawPicker(float x, float y, float radius, float bright) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(7425);

        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL_LINE_STRIP);

        for (int i = 0; i < 360; i++) {
            ColorUtility.setColor(Color.HSBtoRGB(i / 360f, 0, bright));
            glVertex2d(x, y);
            ColorUtility.setColor(Color.HSBtoRGB(i / 360f, 1, bright));
            glVertex2d(
                    x + Math.sin(Math.toRadians(i)) * radius,
                    y + Math.cos(Math.toRadians(i)) * radius
            );
        }
        GL11.glEnd();

        GL11.glBegin(GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            ColorUtility.setColor(Color.HSBtoRGB(i / 360f, 1, bright));
            glVertex2d(
                    x + Math.sin(Math.toRadians(i)) * radius,
                    y + Math.cos(Math.toRadians(i)) * radius
            );
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glShadeModel(7424);



        GL11.glPopMatrix();

    }
    @Override
    public boolean isVisible() {
        return colorSetting.isVisible();
    }
}
