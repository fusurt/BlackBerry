package wtf.expensive.modules.impl.render;

import wtf.expensive.command.impl.CommandNameProtect;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;

@ModuleAnnotation(name = "NameProtect", type = Type.RENDER)
public class NameProtect extends Module {
  public static  String protectName = CommandNameProtect.canChange ? CommandNameProtect.current : "§6Protected";

    public static BooleanSetting youtuber = new BooleanSetting("Youtuber", false);

}
