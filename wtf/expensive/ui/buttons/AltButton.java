package wtf.expensive.ui.buttons;

import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import wtf.expensive.ui.CustomButton;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.UUID;

public class AltButton extends CustomButton {
    public AltButton() {
        super(100, 15, "Alt Manager");
    }

    public boolean open;
    public float animation;
    public boolean printing;
    public String print = "";

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        RenderUtility.drawRect(x, y, width, height, new Color(30, 30, 30, 135).getRGB());
        Fonts.MONTSERRAT14.drawCenteredString(text, x + width / 2, y + 6f, isHovered(mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 255).getRGB());

        animation = AnimationMath.fast(animation, open ? 25 : 0, 15f);

        height = 15 + animation;

        if (animation > 1) {
            y -= 3;
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates(x, y, width, height);
            Fonts.hack14.drawString("name", x + 5, y + 6f + 16, HoveringMath.isHovering(x + 5, y + 6f + 16, Fonts.hack14.getStringWidth("name"), 5, mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 160).getRGB());
            RenderUtility.drawRect(x + 4, y + 12f + 16, width - 8, 12, new Color(0, 0, 0, 100).getRGB());
            Fonts.hack14.drawString(print + (printing ? (System.currentTimeMillis() % 1000 > 500) ? "" : "_" : ""), x + 6, y + 17f + 16, new Color(255, 255, 255, 255).getRGB());
            {
                SmartScissor.push();
                SmartScissor.setFromComponentCoordinates(x + width - 72 , y + 3f + 16, Fonts.hack14.getStringWidth(mc.session.getUsername()) + 40, 9);
                Fonts.hack14.drawString(mc.session.getUsername(), x + width - 4 - Fonts.hack14.getStringWidth(mc.session.getUsername()), y + 6f + 16, HoveringMath.isHovering(x + width - 4 - Fonts.hack14.getStringWidth(mc.session.getUsername()), y + 6f + 16, Fonts.hack14.getStringWidth(mc.session.getUsername()), 5, mouseX, mouseY) ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 160).getRGB());
                SmartScissor.unset();
                SmartScissor.pop();
            }
            SmartScissor.unset();
            SmartScissor.pop();
            y += 3;
        }

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (printing) {
            if (keyCode == 1) {
                printing = false;
                mc.session = new Session("EXP" +RandomStringUtils.randomAlphabetic(MathUtility.intRandom(5, 8)), UUID.randomUUID().toString(), "", "mojang");
                print = "";
            } else if (keyCode == 28) {
                printing = false;
                if (!print.isEmpty())
                    mc.session = new Session(print, UUID.randomUUID().toString(), "", "mojang");
                print = "";
            }
            if (keyCode == 14) {
                if (print.length() > 0) {
                    print = print.substring(0, print.length() - 1);
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && keyCode == Keyboard.KEY_V) {
                String clipboardText;
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);

                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        print += clipboardText;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                print += typedChar;
            }
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered(mouseX, mouseY, 15))
            open = !open;

        if (HoveringMath.isHovering(x + 4, y + 12f + 16, width - 8, 12, mouseX, mouseY)) {
            printing = true;
        }


    }
}
