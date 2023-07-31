package wtf.expensive.modules.impl.util;

import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;

@ModuleAnnotation(name = "Optimization", type = Type.UTIL)
public class OptimizationModule extends Module {

    public static BooleanSetting grass = new BooleanSetting("Grass", true);
    public BooleanSetting shadow = new BooleanSetting("Shadow", true);
    public BooleanSetting particles = new BooleanSetting("Particles", true);
    public BooleanSetting shaders = new BooleanSetting("Shaders", false);

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

}
