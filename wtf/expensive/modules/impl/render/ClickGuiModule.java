package wtf.expensive.modules.impl.render;

import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.utility.color.ColorUtility;

import java.awt.*;

@ModuleAnnotation(name = "ClickGui", type = Type.RENDER)
public class ClickGuiModule extends Module {

    public ColorSetting guiColor = new ColorSetting("Gui Color", ColorUtility.rgba(245, 123, 245, 255));
    public static  BooleanSetting guiAnimations = new BooleanSetting("Gui Animation", true);

    public ClickGuiModule() {
        bind = Keyboard.KEY_RSHIFT;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Expensive.getInstance().csGui);
        Expensive.getInstance().getModuleManager().get(ClickGuiModule.class).setState(false);

    }

    public static Color getColor() {
        return ((ClickGuiModule) Expensive.getInstance().getModuleManager().get(ClickGuiModule.class)).guiColor.getColor();
    }



}
