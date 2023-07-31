package wtf.expensive.ui.csgo.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.ui.csgo.Element;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;
import wtf.expensive.utility.util.ChatUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleElement extends Element {

    public Module module;
    public float animation = 0;
    public boolean extended, binding;
    public List<Element> elements = new ArrayList<>();
    public Framebuffer framebuffer = new Framebuffer(1, 1, true);

    public float renderAnimation = 0;

    public ModuleElement(Module module) {
        this.module = module;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                elements.add(new BooleanElement(this, (BooleanSetting) setting));
            }
            if (setting instanceof SliderSetting) {
                elements.add(new SliderElement(this, (SliderSetting) setting));
            }
            if (setting instanceof ModeSetting) {
                elements.add(new ModeElement((ModeSetting) setting, this));
            }
            if (setting instanceof MultiBoxSetting) {
                elements.add(new ListElement((MultiBoxSetting) setting, this));
            }
            if (setting instanceof ColorSetting) {
                elements.add(new ColorElement(this, (ColorSetting) setting));
            }
        }
    }

    public float xA;
    public float yA;

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        int offset = 0;
        if (extended) {
            for (Element e : elements) {
                if (!e.isVisible()) continue;
                offset += e.height;
            }
        }

        renderAnimation = AnimationMath.fast(renderAnimation, extended ? offset : 0, 15f);
        animation = AnimationMath.fast(animation, module.state ? isHovered(mouseX, mouseY, width, offset + height) ? 1.5f : 1 : isHovered(mouseX, mouseY, width, offset + height) ? 0.25f : 0, 5f);
        Color c = ColorUtility.interpolateColorC(new Color(19, 10, 10, 255).brighter().getRGB(), new Color(22, 22, 22, 255).brighter().brighter().brighter().getRGB(), animation);
        RiseShaders.RQ.draw(x, y, width, height + renderAnimation, 4, c);



       Fonts.nunito14.drawString(binding ? "Press a key.. " + Keyboard.getKeyName(module.bind) : module.name, x + 5, y + 5.5f, new Color(255, 255, 255, 255).getRGB());
        offset = 0;
        if (renderAnimation > 1) {
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates((int) x, (int) y, (int) width, (int) (height + renderAnimation));
            for (Element e : elements) {
                if (!e.isVisible()) continue;
                e.x = x;
                e.y = y + 15 + offset;
                e.width = width;
                e.height = 16;
                e.draw(mouseX, mouseY);
                offset += e.height;
            }
            SmartScissor.unset();
            SmartScissor.pop();
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (binding && mouseButton > 2) {
            module.mouseBind = mouseButton;
            ChatUtility.addChatMessage(ChatFormatting.GREEN + "Bound " + module.name + " to mouse " + mouseButton);
            binding = false;
        }


        if (isHovered(mouseX, mouseY) && mouseButton == 0) {
            module.toggle();
        }

        if (isHovered(mouseX, mouseY) && mouseButton == 1) {
            extended = !extended;
        }
        if (isHovered(mouseX, mouseY) && mouseButton == 2) {
            binding = true;
        }
        if (extended) {
            for (Element e : elements) {
                e.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (extended) {
            for (Element e : elements) {
                e.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {


        if (binding) {
            if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_ESCAPE) {
                module.bind = 0;
                binding = false;
                return;
            }

            module.bind = keyCode;
            binding = false;

        }

    }
}
