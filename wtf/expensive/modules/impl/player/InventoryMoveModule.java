package wtf.expensive.modules.impl.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;

@ModuleAnnotation(name = "Inventory Move", type = Type.PLAYER)
public class InventoryMoveModule extends Module {

    public static BooleanSetting safe = new BooleanSetting("Matrix Safe", false);
    private final EventListener<EventUpdate> onUpdate = e -> {

        KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint};

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign)
            return;

        for (KeyBinding keyBinding : keys) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }

    };

}
