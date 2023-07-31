package wtf.expensive.ui.csgo.elements;

import wtf.expensive.modules.impl.render.ClickGuiModule;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;

import java.awt.*;

public class BooleanElement extends Element {
    public ModuleElement moduleElement;
    public BooleanSetting setting;
public float animation = 0;
    public BooleanElement(ModuleElement moduleElement, BooleanSetting setting) {
        this.moduleElement = moduleElement;
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        animation = AnimationMath.fast(animation, setting.state ? -1 : 0, 15f);
        Fonts.nunito14.drawString(setting.getName(), x + 5, y + 5.5f, -1);
        Color c = ColorUtility.interpolateColorC(new Color(46, 36, 79,255).getRGB(), ClickGuiModule.getColor().getRGB(), Math.abs(animation));
        RenderUtility.drawRound(x + width - 18, y + 6, 13, 6, 3, ColorUtility.rgba(15, 15, 15, 255));
        RenderUtility.drawRoundCircle(x + width - 14 - animation * 5, y + 9f, 2.5f, c.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered(mouseX, mouseY)) {
            setting.state = (!setting.get());
        }
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
