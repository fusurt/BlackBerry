package wtf.expensive.ui.buttons;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import wtf.expensive.ui.CustomButton;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;

import java.awt.*;

public class MultiButton extends CustomButton {

    public MultiButton() {
        super(100, 15, "Multiplayer");
    }

    float animationPresets;
    boolean isPreset;

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        String[] presets = new String[]{
                "ReallyWorld",
                "WhiteHVH",
                "MST",
                "SunRise",
                "StormHVH",
                "NexusGrief",
                "MineBlaze",
                "Jartex",
                "PikaNetwork",
                "InfinityHVH",
        };

        RenderUtility.drawRect(x, y, width, height, new Color(30, 30, 30, 135).getRGB());

        Fonts.MONTSERRAT14.drawCenteredString(text, x + width / 2, y + 6f, isHovered(mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 255).getRGB());
        animationPresets = AnimationMath.fast(animationPresets, isPreset ? presets.length * 14 : 0, 15);
        height = 15 + animationPresets;
        if (animationPresets > 1) {
            y -= 3;
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates(x, y, width, 15 + animationPresets);
            for (int j = 0; j < presets.length; j++) {
                Fonts.hack14.drawString(presets[j], x + 5, y + 6f + 16 + (j * 14), HoveringMath.isHovering(x + 5, y + 4f + 16 + (j * 14), width - 8, 8, mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 100).getRGB());
            }

            SmartScissor.unset();
            SmartScissor.pop();
            y += 3;
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered(mouseX, mouseY, 15) && mouseButton == 1) {
            isPreset = !isPreset;
        }
        if (isHovered(mouseX, mouseY, 15) && mouseButton == 0) {
            mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen));
        }
        String[] presets = new String[]{
                "play.reallyworld.ru",
                "play.whitehvh.ru",
                "mstnw.net",
                "play.sunmc.ru",
                "mc.stormhvh.su",
                "ngrief.su",
                "mc.mineblaze.ru",
                "jartex.fun",
                "play.pika-network.net",
                "mc.infinityhvh.ru"
        };
        if (isPreset) {
            y -= 3;
            for (int j = 0; j < presets.length; j++) {
                if (HoveringMath.isHovering(x + 5, y + 4f + 16 + (j * 14), width - 8, 8, mouseX, mouseY)) {
                    mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(mc.currentScreen), mc, new ServerData(presets[j], presets[j], false)));
                }
            }
            y += 3;
        }

    }
}
