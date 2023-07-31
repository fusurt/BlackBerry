package wtf.expensive.ui.csgo.elements;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wtf.expensive.Expensive;
import wtf.expensive.modules.impl.render.ClickGuiModule;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;

public class SliderElement extends Element {
    public ModuleElement moduleElement;
    public SliderSetting setting;
    public float animation = 0;
    public boolean isDragging;

    public SliderElement(ModuleElement moduleElement, SliderSetting setting) {
        this.moduleElement = moduleElement;
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        Fonts.nunito12.drawString(setting.getName(), x + 5, y + 5f, -1);
        RenderUtility.drawRound(x + 4, y + 14, width - 8, 2, 2, new Color(255, 255, 255, 42).getRGB());

        float sliderWidth = (float) ((setting.get() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue()) * (width - 8));
        animation = AnimationMath.fast(animation, sliderWidth, 15f);
        RenderUtility.drawGradientRound(x + 4, y + 14, animation, 2, 2, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), 255).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), 255).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), 255).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), 255).getRGB());

        if (isDragging) {
            if (!Mouse.isButtonDown(0)) {
                this.isDragging = false;
            }
            float sliderValue = (float) MathHelper.clamp(MathUtility.round((float) ((mouseX - x - 1) / (width - 8) * (setting.getMaxValue() - setting.getMinValue()) + setting.getMinValue()), setting.getIncrement()), setting.getMinValue(), setting.getMaxValue());
            setting.setValueNumber(sliderValue);
        }

        Fonts.nunito12.drawString(setting.get() + "", x + 130 - Fonts.nunito12.getStringWidth(String.valueOf(setting.get())), y + 5f, -1);


    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            isDragging = false;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY) && mouseButton == 0) {
            isDragging = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
