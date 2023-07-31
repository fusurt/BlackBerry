package wtf.expensive.modules.impl.render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.BossInfo;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "World", type = Type.RENDER)
public class WorldModule extends Module {

    public MultiBoxSetting modes = new MultiBoxSetting("Modes", new String[]{"Time", "Weather", "Fog", "World Color"});


    public ColorSetting worldColor = new ColorSetting("World Color", -1, () -> modes.get(3));
    public ColorSetting fogColor = new ColorSetting("Fog Color", -1, () -> modes.get(2));
    public ColorSetting weatherColor = new ColorSetting("Weather Color", -1, () -> modes.get(1));
    public SliderSetting time = new SliderSetting("Time", 0, 0, 24000, 200, () -> modes.get(0));

    public EventListener<EventPacket> onPacket = e -> {
        if (e.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (e.getPacket() instanceof SPacketTimeUpdate) {
                if (modes.get(0)) {
                    e.setCancelled(true);
                }
            }
        }
    };

    public EventListener<EventUpdate> onUpdate = e -> {
        if (modes.get(0)) {
            setDisplayName("Time: " + (int) time.get());
            mc.world.setWorldTime((long) time.get());
        }
    };

    public boolean isWeather() {
        return Expensive.getInstance().getModuleManager().world.state && Expensive.getInstance().getModuleManager().world.modes.get(1);
    }

    public boolean isFog() {
        return Expensive.getInstance().getModuleManager().world.state && Expensive.getInstance().getModuleManager().world.modes.get(2);
    }

    public boolean isWorldColor() {
        return Expensive.getInstance().getModuleManager().world.state && Expensive.getInstance().getModuleManager().world.modes.get(3);
    }



}
