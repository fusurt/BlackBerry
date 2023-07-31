package wtf.expensive.modules.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.MobEffects;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.game.EventOverlay;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

@ModuleAnnotation(name = "No Render", type = Type.RENDER)
public class NoRenderModule extends Module {


    public static MultiBoxSetting element = new MultiBoxSetting("Element",
            new String[]{"Scoreboard", "Totem", "Fire Overlay", "Armor Stand", "Boss Bar", "Bad Effects", "Rain", "Light", "Fog"});


    private final EventListener<EventOverlay> e = e -> {
        if ((element.get(1) && e.getOverlayType() == EventOverlay.OverlayType.TotemAnimation)
                || (element.get(2) && e.getOverlayType() == EventOverlay.OverlayType.Fire)
                || (element.get(4) && e.getOverlayType() == EventOverlay.OverlayType.BossBar)
                || (element.get(7) && e.getOverlayType() == EventOverlay.OverlayType.Light)
                || (element.get(8) && e.getOverlayType() == EventOverlay.OverlayType.Fog)) {
            e.cancel();
        }
    };

    private final EventListener<EventUpdate> onUpdate = e -> {
        if (element.get(6) && mc.world.isRaining()) {
            mc.world.setRainStrength(0);
            mc.world.setThunderStrength(0);
        }
        if (element.get(5) && mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isPotionActive(MobEffects.NAUSEA)) {
            mc.player.removePotionEffect(MobEffects.NAUSEA);
            mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (element.get(3)) {
            if (mc.player == null || mc.world == null) {
                return;
            }
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityArmorStand)) continue;
                mc.world.removeEntity(entity);
            }
        }
    };
}
