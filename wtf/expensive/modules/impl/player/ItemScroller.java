package wtf.expensive.modules.impl.player;

import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "Item Scroller", type = Type.PLAYER)
public class ItemScroller extends Module {

    public static SliderSetting delay = new SliderSetting("Delay", 80, 0, 1000, 1);

}
