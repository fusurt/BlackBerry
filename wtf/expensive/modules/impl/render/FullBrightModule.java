package wtf.expensive.modules.impl.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "FullBright", type = Type.RENDER)
public class FullBrightModule extends Module {

    private final EventListener<EventUpdate> updateEventListener = e -> {
        mc.gameSettings.gammaSetting = 100;
    };

    @Override
    public void onDisable() {

        if (mc.player != null) {
            mc.gameSettings.gammaSetting = 0.1f;
        }

        super.onDisable();
    }
}
