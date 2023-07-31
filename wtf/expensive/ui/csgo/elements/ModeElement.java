package wtf.expensive.ui.csgo.elements;

import wtf.expensive.modules.impl.render.ClickGuiModule;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;

import java.awt.*;

public class ModeElement extends Element {
    public ModeSetting modeSetting;
    public ModuleElement moduleElement;
    public boolean extended;
    public float animation = 0;

    public ModeElement(ModeSetting modeSetting, ModuleElement moduleElement) {
        this.modeSetting = modeSetting;
        this.moduleElement = moduleElement;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        float offset = extended ? modeSetting.modes.size() * 10 : 0;
        animation = AnimationMath.fast(animation, extended ? offset : 0, 15f);
        this.height = 15 + animation;
        super.draw(mouseX, mouseY);
        Fonts.nunito14.drawString(modeSetting.getName(), x + 5, y + 5, 0xFFFFFFFF);



        float maxWidthString = (float) modeSetting.modes.stream().mapToDouble(Fonts.nunito14::getStringWidth).max().orElse(0);
        RiseShaders.RQ.draw(x + width - 5 - maxWidthString - 10, y + 2.5f, maxWidthString + 10, 10 + animation, 3, new Color(46, 36, 79, 255));
        Fonts.nunito14.drawCenteredString(modeSetting.get(), x + width - 5 - maxWidthString / 2 - 5, y + 5, ClickGuiModule.getColor().getRGB());

        float modeOffset = 0;
        if (animation > 1) {
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates(x + width - 5 - maxWidthString - 10, y + 2.5f, maxWidthString + 10, 10 + animation);
            for (String mode : modeSetting.getModes()) {
                Fonts.nunito14.drawCenteredString(mode, x + width - 5 - maxWidthString / 2 - 5, y + 15 + modeOffset, modeSetting.get().equals(mode) ? ClickGuiModule.getColor().getRGB() : 0xFFFFFFFF);
                modeOffset += 10;
            }
            SmartScissor.unset();
            SmartScissor.pop();
        }



    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float maxWidthString = (float) modeSetting.modes.stream().mapToDouble(Fonts.nunito14::getStringWidth).max().orElse(0);
        if (HoveringMath.isHovering(x + width - 5 - maxWidthString - 10, y + 2.5f, maxWidthString + 10, 10, mouseX , mouseY)) {
            extended = !extended;
        }
        if (extended) {
            float modeOffset = 0;
            for (String mode : modeSetting.getModes()) {
                if (HoveringMath.isHovering(x + width - 5 - maxWidthString - 10, y + 2.5f + 10 + modeOffset, maxWidthString + 10, 10, mouseX , mouseY)) {
                    modeSetting.set(mode);
                }
                modeOffset += 10;
            }
        }

    }

    @Override
    public boolean isVisible() {
        return modeSetting.isVisible();
    }

}
