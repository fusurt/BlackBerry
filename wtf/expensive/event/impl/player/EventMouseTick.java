package wtf.expensive.event.impl.player;

import wtf.expensive.event.Event;

public class EventMouseTick extends Event {

    private int button;

    public EventMouseTick(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
