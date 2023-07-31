package wtf.expensive.ui.buttons;

import net.minecraft.client.gui.GuiMultiplayer;
import wtf.expensive.ui.CustomButton;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;

import java.awt.*;

public class ExitButton extends CustomButton {
    public ExitButton() {
        super(100, 15, "Exit");
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);


        RenderUtility.drawRect(x, y, width, height, new Color(30, 30, 30, 135).getRGB());
        Fonts.MONTSERRAT14.drawCenteredString(text, x + width / 2, y + 6f, isHovered(mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 255).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mc.shutdownMinecraftApplet();
    }

}