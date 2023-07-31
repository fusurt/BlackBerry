package wtf.expensive.utility.math;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec2f;

public class ScaleMath {

    private int scale;

    public ScaleMath(int scale){
        this.scale = scale;
    }

    public void pushScale(){
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering(2);
    }

    public int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }

    public float calc(float value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }

    public double calc(double value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }


    public void popScale(){
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
    }

    public Vec2i getMouse(int mouseX, int mouseY, ScaledResolution rs){
        return new Vec2i(mouseX * rs.getScaleFactor() / this.scale, mouseY * rs.getScaleFactor() / this.scale);
    }
    public Vec2f getMouse(float mouseX, float mouseY, ScaledResolution rs){
        return new Vec2f(mouseX * rs.getScaleFactor() / this.scale, mouseY * rs.getScaleFactor() / this.scale);
    }

    public int getScale(){
        return this.scale;
    }
    public void setScale(int scale){
        this.scale = scale;
    }
}
