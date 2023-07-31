package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.function.Supplier;

public class TextSetting extends Setting {
    public String text;

    public TextSetting(String name, String text) {
        super(name, text);
        this.text = text;
        this.setVisible(() -> true);
    }

    public TextSetting(String name, String text, Supplier<Boolean> visible) {
        super(name, text);
        this.text = text;
        setVisible(visible);
    }

    public String get() {
        return text;
    }

}
