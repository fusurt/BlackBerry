package wtf.expensive.modules.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configurable {
    public final ArrayList<Setting> settingList = new ArrayList<>();

    public final void add(Setting... options) {
        this.settingList.addAll(Arrays.asList(options));
    }
    public List<Setting> get() {
        return this.settingList;
    }

}
