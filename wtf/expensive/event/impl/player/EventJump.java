package wtf.expensive.event.impl.player;

import wtf.expensive.event.Event;

public class EventJump extends Event {

    public float jumpHeight = 0;

    public EventJump(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }



}