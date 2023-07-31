package wtf.expensive.modules.settings;

import java.util.function.Supplier;

public class Setting {

    private String name;
    private Object value;
    protected Supplier<Boolean> visible;

    public Setting(String name, Object value){
        this.name = name;
        this.value = value;
    }
    public boolean isVisible() {
        return visible.get();
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
