package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class DepSetting extends Setting {

    public List<Setting> settings = new ArrayList<>();

    public DepSetting(String name, Setting... settings) {
        super(name, settings);
    }
}
