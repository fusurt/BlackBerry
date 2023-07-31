package wtf.expensive.event.impl.player;

import net.minecraft.item.Item;
import wtf.expensive.event.Event;

public class EventCalculateCooldown extends Event {
    private Item stack;
    private float cooldown;

    public EventCalculateCooldown(Item stack) {
        this.stack = stack;
    }

    public float getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(float f) {
        this.cooldown = f;
    }

    public Item getStack() {
        return this.stack;
    }
}
