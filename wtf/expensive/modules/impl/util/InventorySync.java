package wtf.expensive.modules.impl.util;

import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "Inventory Sync", type = Type.UTIL)
public class InventorySync extends Module {

    public short action;

    // Rewritten code
    public final EventListener<EventPacket> onPacketReceiveEvent = event -> {
        if (event.getPacketType() != EventPacket.PacketType.RECEIVE) return;
        if (mc.player == null) return;
        final Packet<?> packet = event.getPacket();

        if (packet instanceof SPacketConfirmTransaction wrapper) {
            final Container inventory = mc.player.inventoryContainer;
            if (inventory != null && wrapper != null && wrapper.getWindowId() == inventory.windowId) {
                int action = wrapper.getActionNumber();
                if (action > 0 && action < inventory.transactionID) inventory.transactionID = (short) (action + 1);
            }
        }

        // prevents the server from changing slots
        if (packet instanceof SPacketHeldItemChange) {
            SPacketHeldItemChange packetHeldItemChange = (SPacketHeldItemChange) event.getPacket();
            if (packetHeldItemChange.getHeldItemHotbarIndex() != mc.player.inventory.currentItem) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                event.cancel();
            }
        }
    };
}
