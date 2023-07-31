package wtf.expensive.styles;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StyleManager {
    public List<Style> styles = new ArrayList<>();
    private Style currentStyle = null;

    public StyleManager() {
        styles.add(new Style("", new Color(64, 174, 252).brighter(), new Color(241, 95, 233).brighter()));
        styles.add(new Style("",new Color(171, 54, 241), new Color(93, 229, 193).brighter()));
        styles.add(new Style("",new Color(238, 79, 238), new Color(253, 219, 245).brighter()));
        styles.add(new Style("", new Color(37, 50, 94).brighter().brighter().brighter(), new Color(24, 42, 82).brighter().brighter()));
        styles.add(new Style("",new Color (157, 231, 71), new Color(40, 105, 11).brighter()));
        styles.add(new Style("",new Color(236, 110, 173), new Color(238, 12, 114).brighter()));
        styles.add(new Style("",new Color(66, 134, 244), new Color(55, 59, 68).brighter()));
        styles.add(new Style("",new Color(193, 0, 225), new Color(33, 229, 12).brighter()));
        styles.add(new Style("",new Color(243, 99, 205), new Color(138, 152, 246).brighter()));
        styles.add(new Style("",new Color(9, 255, 217), new Color(7, 166, 246).brighter()));
        styles.add(new Style("", new Color(9, 57, 155).brighter().brighter().brighter(), new Color(0, 120, 255).brighter().brighter()));
        styles.add(new Style("",new Color(243, 17, 164), new Color(97, 4, 95).brighter()));
        styles.add(new Style("",new Color(247, 255, 0), new Color(219, 54, 164).brighter()));
        styles.add(new Style("", new Color(0x2C3E50).brighter(), new Color(0xFD746C).brighter()));
        styles.add(new Style("",new Color(252, 74, 26), new Color(247, 183, 51).brighter()));
        styles.add(new Style("",new Color(9, 42, 253), new Color(215, 10, 239).brighter()));
        styles.add(new Style("",new Color(9, 42, 253), new Color(243, 18, 45).brighter()));
        styles.add(new Style("",  new Color(246, 224, 10), new Color(88, 77, 225).brighter()));
        styles.add(new Style("", new Color(0x42275a).brighter().brighter(), new Color(0x734b6d).brighter().brighter()));
        styles.add(new Style("", new Color(0x773402).brighter().brighter(), new Color(0xBB5D01).brighter().brighter()));
        currentStyle = styles.get(0);
    }
    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }
    public void setCurrentStyle(Style style) {
        currentStyle = style;
    }

    public Style getCurrentStyle() {
        return currentStyle;
    }
}