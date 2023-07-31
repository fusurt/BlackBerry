package wtf.expensive.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import wtf.expensive.event.Event;

public class EventDraw extends Event {

    public RenderType type;

    public float partialTicks;
    public ScaledResolution sr;

    public EventDraw(RenderType type, float partialTicks, ScaledResolution sr) {
        this.type = type;
        if (type == RenderType.RENDER) {
            this.partialTicks = partialTicks;
        } else {
            this.partialTicks = partialTicks;
            this.sr = sr;
        }
    }

    public enum RenderType {
        DISPLAY, RENDER
    }
}
