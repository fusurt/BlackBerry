package wtf.expensive.modules.impl.player;

import net.minecraft.init.MobEffects;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "Anti Levitation", type = Type.PLAYER)
public class AntiLevitation extends Module {

    private final EventListener<EventUpdate> eventUpdateEventListener = e -> {
        if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
            mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    };

}
