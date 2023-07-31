package wtf.expensive.event.impl.player;

import net.minecraft.entity.Entity;
import wtf.expensive.event.Event;

public class EventInteractEntity extends Event {
    private Entity entity;
    private boolean canceled;

    public EventInteractEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
