package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.awt.*;
import java.util.function.Supplier;

public class ColorSetting extends Setting {

    private int color;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        super(name, color);
        this.color = color;
        setVisible(visible);
    }

    public ColorSetting(String name, int color) {
        super(name, color);
        this.color = color;
        setVisible(() -> true);
    }

    public int get() {
        return color;
    }

    public Color getColor() {
        return new Color(color);
    }

    public void setColorValue(int color) {
        this.color = color;
    }


}
