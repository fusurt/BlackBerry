package wtf.expensive.event.impl.game;

import wtf.expensive.event.Event;

public class EventOverlay extends Event {

    private final OverlayType overlayType;

    public EventOverlay(OverlayType overlayType) {
        this.overlayType = overlayType;
    }

    public OverlayType getOverlayType() {
        return overlayType;
    }

    public enum OverlayType {
        TotemAnimation, Fire, BossBar, Fog, Light
    }

}
