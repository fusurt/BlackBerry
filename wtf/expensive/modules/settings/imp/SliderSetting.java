package wtf.expensive.modules.settings.imp;


import wtf.expensive.modules.settings.Setting;

import java.util.function.Supplier;

public class SliderSetting extends Setting {

    public float current, min, max, increment;

    public SliderSetting(String name, float value, float min, float max, float increment) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.current = value;
        this.increment = increment;
        this.setVisible(() -> true);
    }
    public SliderSetting(String name, float value, float min, float max, float increment, Supplier<Boolean> visible) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.current = value;
        this.increment = increment;
        setVisible(visible);
    }
    public double getMinValue() {
        return min;
    }

    public void setMinValue(float minimum) {
        this.min = minimum;
    }

    public double getMaxValue() {
        return max;
    }

    public void setMaxValue(float maximum) {
        this.max = maximum;
    }

    public float get() {
        return current;
    }

    public void setValueNumber(float current) {
        this.current = current;
    }

    public double getIncrement() {
        return increment;
    }
}
