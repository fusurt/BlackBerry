package wtf.expensive.modules.impl.combat;

import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "HitBox", type = Type.COMBAT)
public class HitBoxModule extends Module {


    public static SliderSetting size = new SliderSetting("Size", 0.2f, 0.f, 3.f, 0.05f);

}
