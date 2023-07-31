package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiBoxSetting extends Setting {
    public List<Boolean> selectedValues = new ArrayList<>();

    public String[] values;

    public MultiBoxSetting(String name, String[] values) {
        this(name, values, null);
        setVisible(() -> true);

    }

    public MultiBoxSetting(String name, String[] values, Supplier<Boolean> condition) {
        super(name, condition);
        this.values = values;
        for (int i = 0; i < values.length; i++) {
            this.selectedValues.add(false);
        }

        setVisible(condition);
    }

    public boolean get(int id) {
        return this.selectedValues.get(id);
    }

    public boolean isEnabled() {
        return this.isVisible();
    }

}
