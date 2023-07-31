package wtf.expensive.modules.impl.render;

import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;

@ModuleAnnotation(name = "Swing Animation", type = Type.RENDER)
public class SwingAnimationModule extends Module {

    public static ModeSetting swordAnim = new ModeSetting("Mode", "Self", "Smooth", "Self", "Block");

    public static SliderSetting angle = new SliderSetting("Angle", 100, 0, 360, 1, () -> swordAnim.is("Self") || swordAnim.is("Block"));
    public static SliderSetting swipePower = new SliderSetting("Swipe Power", 8, 1, 10, 1, () -> swordAnim.is("Self") || swordAnim.is("Block"));
    public static SliderSetting swipeSpeed = new SliderSetting("Swipe Speed", 11, 1, 20, 1);

    public static SliderSetting right_x = new SliderSetting("RightX", 0.0F, -2, 2, 0.1F);
    public static SliderSetting right_y = new SliderSetting("RightY", 0.0F, -2, 2, 0.1F);
    public static SliderSetting right_z = new SliderSetting("RightZ", 0.0F, -2, 2, 0.1F);
    public static SliderSetting left_x = new SliderSetting("LeftX", 0.0F, -2, 2, 0.1F);
    public static SliderSetting left_y = new SliderSetting("LeftY", 0.0F, -2, 2, 0.1F);
    public static SliderSetting left_z = new SliderSetting("LeftZ", 0.0F, -2, 2, 0.1F);


    private final EventListener<EventUpdate> onUpdate = e -> {
        setDisplayName(swordAnim.get());
    };
}
