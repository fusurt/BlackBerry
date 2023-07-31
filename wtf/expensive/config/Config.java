package wtf.expensive.config;

import com.google.gson.JsonObject;
import wtf.expensive.Expensive;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.utility.util.ChatUtility;

import java.io.File;

public final class Config implements ConfigUpdater {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".cfg");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    @Override
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : Expensive.instance.getModuleManager().getModules()) {
            modulesObject.add(module.name, module.save());
        }

        jsonObject.add("Features", modulesObject);

        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Module module : Expensive.instance.getModuleManager().getModules()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.name));
            }
        }
    }

}