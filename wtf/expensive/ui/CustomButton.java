package wtf.expensive.ui;

import net.minecraft.client.Minecraft;

public class CustomButton {

    public float x;
    public float y;
    public Minecraft mc = Minecraft.getMinecraft();

    public float width;
    public float height;

    public String text;

    public CustomButton(float width, float height, String text) {
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public void draw(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isHovered(int mouseX, int mouseY, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void keyTyped(char typedChar, int keyCode) {

    }





}
