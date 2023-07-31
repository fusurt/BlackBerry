package wtf.expensive.event;

public interface EventListener<T> {
    void call(T event);
}
