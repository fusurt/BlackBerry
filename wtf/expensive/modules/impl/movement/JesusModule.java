package wtf.expensive.modules.impl.movement;

import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "Jesus", type = Type.MOVEMENT)
public class JesusModule extends Module {

    public static SliderSetting speed = new SliderSetting("Speed", 1.1f, 0.1F, 1.1f, 0.01F);
    private BooleanSetting disablerCheck = new BooleanSetting("Disabler Check", true);


}
