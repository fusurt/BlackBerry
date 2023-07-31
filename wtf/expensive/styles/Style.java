package wtf.expensive.styles;

import wtf.expensive.Expensive;
import wtf.expensive.utility.color.ColorUtility;

import java.awt.*;

public class Style {
    public String name;
    public Color[] colors;

    public Style(String name, Color... colors) {
        this.name = name;
        this.colors = colors;
    }


    public int getColor(int index) {
        return ColorUtility.gradient((int) Expensive.getInstance().getModuleManager().arraylist.colorSpeed.get(),
               index *  (int)  Expensive.getInstance().getModuleManager().arraylist.colorOffset.get(), colors).getRGB();
    }

}
