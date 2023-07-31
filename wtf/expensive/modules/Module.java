package wtf.expensive.modules;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.Expensive;
import wtf.expensive.modules.impl.render.Notifications;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.styles.StyleConfig;
import wtf.expensive.utility.Utility;
import wtf.expensive.utility.drag.DragManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module implements Utility {


    protected ModuleAnnotation info = this.getClass().getAnnotation(ModuleAnnotation.class);

    public String name;
    public Type category;
    public int bind, mouseBind;
    public float animation;
    public boolean state;
    public String displayName;

    protected Module() {
        name = info.name();
        category = info.type();
        state = false;
        bind = 0;
        mouseBind = 0;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName(boolean flip) {
        if (Expensive.getInstance().getModuleManager().arraylist.suffix.is("None")) {
            return name;
        }
        return name;

    }


    public void setState(boolean state) {
        if (state) {
            Expensive.getInstance().getEventProtocol().register(this);
        } else {
            Expensive.getInstance().getEventProtocol().unregister(this);
        }
        this.state = state;

    }

    protected void toggleSilent() {
        this.state = !this.state;

        if (mc.player != null) {
            if (this.state) {
                this.onEnable();
            } else {
                this.onDisable();
            }
        }

    }

    public void toggle() {
        toggleSilent();
        if (!name.equals("ClickGui"))
            Notifications.notify("Module Debug", name + " " + "was " + (state ? TextFormatting.GREEN
                    + "enabled." : TextFormatting.RED + "disabled."), Notifications.Notify.NotifyType.INFORMATION, 1);
    }

    protected void onEnable() {
        if (mc.player != null) {
            StyleConfig.save();
            DragManager.save();
            Expensive.getInstance().config.saveConfig("default");
            Expensive.getInstance().getEventProtocol().register(this);
        }

    }

    protected void onDisable() {
        if (mc.player != null) {
            StyleConfig.save();
            DragManager.save();
            Expensive.getInstance().config.saveConfig("default");
            Expensive.getInstance().getEventProtocol().unregister(this);
        }

    }

    public List<Setting> getSettings() {
        return Arrays.stream(this.getClass().getDeclaredFields()).map(field -> {
            try {
                field.setAccessible(true);
                return field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(field -> field instanceof Setting).map(field -> (Setting) field).collect(Collectors.toList());
    }

    public JsonObject save() {

        JsonObject object = new JsonObject();

        object.addProperty("bind", bind);
        object.addProperty("mouseBind", mouseBind);
        object.addProperty("state", state);

        JsonObject settings = new JsonObject();

        for (Setting setting : getSettings()) {
            if (setting instanceof BooleanSetting) {
                settings.addProperty(setting.getName(), ((BooleanSetting) setting).get());
            }
            if (setting instanceof SliderSetting) {
                settings.addProperty(setting.getName(), ((SliderSetting) setting).get());
            }
            if (setting instanceof ColorSetting) {
                settings.addProperty(setting.getName(), ((ColorSetting) setting).get());
            }
            if (setting instanceof ModeSetting) {
                settings.addProperty(setting.getName(), ((ModeSetting) setting).get());
            }
            if (setting instanceof MultiBoxSetting) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for (String s : ((MultiBoxSetting) setting).values) {
                    if (((MultiBoxSetting) setting).selectedValues.get(i))
                        builder.append(s + "\n");
                    i++;
                }
                settings.addProperty(setting.getName(), builder.toString());
            }
        }
        object.add("settings", settings);
        return object;
    }


    public void load(JsonObject object) {
        if (object != null) {

            if (object.has("bind")) bind = object.get("bind").getAsInt();
            if (object.has("mouseBind")) mouseBind = object.get("mouseBind").getAsInt();
            if (object.has("state")) setState(object.get("state").getAsBoolean());
            for (Setting setting : getSettings()) {
                JsonObject settings = object.getAsJsonObject("settings");
                if (settings == null)
                    continue;
                if (!settings.has(setting.getName()))
                    continue;
                if (setting instanceof BooleanSetting) {
                    ((BooleanSetting) setting).state = (settings.get(setting.getName()).getAsBoolean());
                }
                if (setting instanceof SliderSetting) {
                    ((SliderSetting) setting).current = (settings.get(setting.getName()).getAsFloat());
                }
                if (setting instanceof ColorSetting) {
                    ((ColorSetting) setting).setColorValue(settings.get(setting.getName()).getAsInt());
                }
                if (setting instanceof ModeSetting) {
                    ((ModeSetting) setting).set((settings.get(setting.getName()).getAsString()));
                }
                if (setting instanceof MultiBoxSetting) {
                    for (int i = 0; i < ((MultiBoxSetting) setting).selectedValues.size(); i++) {
                        ((MultiBoxSetting) setting).selectedValues.set(i, false);
                    }
                    int i = 0;
                    String[] strs = settings.get(setting.getName()).getAsString().split("\n");
                    for (String s : ((MultiBoxSetting) setting).values) {
                        for (String str : strs) {
                            if (str.equalsIgnoreCase(s)) {
                                ((MultiBoxSetting) setting).selectedValues.set(i, true);
                            }
                        }
                        i++;
                    }
                }
            }
        }
    }
}
