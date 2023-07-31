package wtf.expensive.modules.settings.imp;


import wtf.expensive.modules.settings.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    public boolean state;

    public BooleanSetting(String name, boolean state) {
        super(name, state);
        this.state = state;
        this.setVisible(() -> true);

    }

    public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
        super(name, state);
        this.state = state;
        setVisible(visible);
    }

    public boolean get() {
        return state;
    }

    public void set(boolean state) {
        this.state = state;
    }
}
