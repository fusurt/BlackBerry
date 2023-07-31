package wtf.expensive.event;

import wtf.expensive.Expensive;

public class EventManager {
    public static void dispatchEvent(Event event) {
        Expensive.getInstance().getEventProtocol().dispatch(event);
    }

}
