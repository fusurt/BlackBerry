package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.function.Supplier;

public class RangeSetting extends Setting {

    public float first, second, min, max, increment;

    public RangeSetting(String name, float first, float second, float min, float max, float increment) {
        super(name, first);
        this.min = min;
        this.max = max;
        this.first = first;
        this.second = second;
        this.increment = increment;
        this.setVisible(() -> true);
    }

    public RangeSetting(String name, float first, float second, float min, float max, float increment, Supplier<Boolean> visible) {
        super(name, first);
        this.min = min;
        this.max = max;
        this.first = first;
        this.second = second;
        this.increment = increment;
        setVisible(visible);
    }

    public float getFirst() {
        return first;
    }

    public float getSecond() {
        return second;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getIncrement() {
        return increment;
    }
}
