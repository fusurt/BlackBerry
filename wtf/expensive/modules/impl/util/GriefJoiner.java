package wtf.expensive.modules.impl.util;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.TextSetting;
import wtf.expensive.utility.util.ChatUtility;

@ModuleAnnotation(name = "GriefJoiner", type = Type.UTIL)
public class GriefJoiner extends Module {
    public ModeSetting grief = new ModeSetting("Grief", "1", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21");

    TimerUtility timerUtility = new TimerUtility();
    private final EventListener<EventUpdate> eventUpdate = e -> {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("reallyworld")) {
            if (mc.player.ticksExisted % 20 == 0) {
                mc.player.sendChatMessage("/grief-" + grief.get());
            }
        } else {
            if (mc.currentScreen instanceof GuiChest) {
                GuiContainer container = (GuiContainer) mc.currentScreen;
                for (int i = 0; i < container.inventorySlots.inventorySlots.size(); i++) {
                    String s = container.inventorySlots.inventorySlots.get(i).getStack().getDisplayName();
                    if (s.contains("GRIEF-" + grief.get())) {
                        if (timerUtility.hasTimeElapsed(50)) {
                            mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.PICKUP, mc.player);
                            timerUtility.reset();
                        }
                    }
                }
            }
        }

    };
}
