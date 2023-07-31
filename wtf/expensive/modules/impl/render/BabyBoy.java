package wtf.expensive.modules.impl.render;

import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

@ModuleAnnotation(name = "BabyBoy", type = Type.RENDER)
public class BabyBoy extends Module {

    public static MultiBoxSetting multiBoxSetting = new MultiBoxSetting("Entities", new String[]{"Players", "Self", "Friends"});

    public static boolean isPlayer() {
        return multiBoxSetting.get(0);
    }

    public static boolean isSelf() {
        return multiBoxSetting.get(1);
    }

    public static boolean isFriend() {
        return multiBoxSetting.get(2);
    }



}
