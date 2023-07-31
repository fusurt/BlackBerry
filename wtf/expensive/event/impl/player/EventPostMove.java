package wtf.expensive.event.impl.player;

import wtf.expensive.event.Event;

public class EventPostMove extends Event {
    private double horizontalMove;

    public EventPostMove(double horizontalMove) {
        this.horizontalMove = horizontalMove;
    }

    public double getHorizontalMove() {
        return this.horizontalMove;
    }
}
