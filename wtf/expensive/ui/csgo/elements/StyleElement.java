package wtf.expensive.ui.csgo.elements;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import wtf.expensive.Expensive;
import wtf.expensive.styles.Style;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.Arrays;

public class StyleElement extends Element {

    public Style style;
    public boolean expanded;
    public Color color;
    public int index;

    public StyleElement(Style st) {
        this.style = st;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        if (expanded) {
            height = 16 * 2;
            if (style == Expensive.getInstance().styleManager.styles.get(Expensive.getInstance().styleManager.styles.size() - 1) && color != null) {
                height = 95;

            }
        }
        if (Expensive.getInstance().styleManager.getCurrentStyle().equals(style)) {
        }
        RenderUtility.drawGradientHorizontal(x - 13, y + 1, width - 60, 16 - 2, 2, style.getColor(50), style.getColor(100));

        if (!expanded)
            color = null;
        if (style == Expensive.getInstance().styleManager.styles.get(Expensive.getInstance().styleManager.styles.size() - 1) && expanded) {

        }
        if (color != null) {
            if (HoveringMath.isHovering(x + 4, y + 14 + 18 + 55, width - 8, 60 - 10, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                hue = (mouseX - (x + 4)) / (width - 8f);
                style.colors[index] = new Color(Color.HSBtoRGB(hue, saturation, brightness));
            }
            if (HoveringMath.isHovering(x + 4, y + 14 + 18, width - 8, 60 - 10, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                float saturation = (mouseY - (y + 14 + 18)) / (60 - 10f);
                saturation = MathHelper.clamp(saturation, 0, 1);
                float brightness = (mouseX - (x + 4)) / (width - 8f);
                brightness = MathHelper.clamp(brightness, 0, 1);
                this.brightness = 1 - saturation;
                this.saturation = brightness;
                style.colors[index] = new Color(Color.HSBtoRGB(hue, this.saturation, this.brightness) );
            }
            hue = MathHelper.clamp(hue, 0, 1);
        }
        Fonts.hack14.drawCenteredString(style.name, x + width / 2, y + 16 / 2f - 1.5f, new Color(255, 255, 255, 255).getRGB());
        index = MathHelper.clamp(index, 0, style.colors.length - 1);

    }

    float hue;
    float saturation;
    float brightness;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered(mouseX, mouseY, width, 16) && mouseButton == 0) {
            Expensive.getInstance().styleManager.setCurrentStyle(style);
        }


        if (expanded && style == Expensive.getInstance().styleManager.styles.get(Expensive.getInstance().styleManager.styles.size() - 1)) {
            if (HoveringMath.isHovering(x + width - 12, y + 4 + 16, 8, 8, mouseX, mouseY) && mouseButton == 0) {
                style.colors = Arrays.copyOf(style.colors, style.colors.length + 1);
                style.colors[style.colors.length - 1] = new Color(255, 255, 255);
            }


            int offset = 0;
            for (Color color : style.colors) {
                if (HoveringMath.isHovering(x + 5 + offset, y + 4 + 16, 8, 8, mouseX, mouseY) && mouseButton == 1) {
                    if (style.colors.length == 1)
                        return;
                    style.colors = Arrays.stream(style.colors).filter(c -> c != color).toArray(Color[]::new);
                    if (index > 0)
                        index--;

                }
                if (HoveringMath.isHovering(x + 5 + offset, y + 4 + 16, 8, 8, mouseX, mouseY) && mouseButton == 0) {
                    if (index < 0) return;
                    this.color = color;
                    index = Arrays.stream(style.colors).toList().indexOf(color);
                    float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

                    hue = hsb[0];
                    saturation = hsb[1];
                    brightness = hsb[2];

                }
                offset += 10;
            }
        }
    }
}
