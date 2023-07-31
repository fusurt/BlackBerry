package wtf.expensive.event.impl.game;

import wtf.expensive.event.Event;

public class EventKeyPress extends Event {

    private final int key;

    public EventKeyPress(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
