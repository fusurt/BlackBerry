package wtf.expensive.styles;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import wtf.expensive.Expensive;
import wtf.expensive.modules.Module;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class StyleConfig {
    public static void save() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "\\expensive\\styles.exp");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Style module : Expensive.getInstance().styleManager.styles) {
            String colors = "";
            for (int i = 0; i < module.colors.length; i++) {
                colors += module.colors[i].getRGB();
                if (i != module.colors.length - 1) {
                    colors += ",";
                }
            }
            JsonObject object = new JsonObject();
            object.addProperty("color", colors);
            object.addProperty("selected", Expensive.getInstance().styleManager.getCurrentStyle() == module);
            modulesObject.add(module.name, object);
        }

        jsonObject.add("styles", modulesObject);

        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(contentPrettyPrint);
            writer.close();
        } catch (IOException e) {

        }
    }


    public static void load() {
        File file = new File("StyleConfig.json");
        String text;
        try {
            text = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            save();
            return;
        }



        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(text);

        if (object.has("styles")) {
            JsonObject modulesObject = object.getAsJsonObject("styles");
            for (Style module : Expensive.getInstance().styleManager.styles) {
                String colors = modulesObject.getAsJsonObject(module.name).get("color").getAsString();
                boolean selected = modulesObject.getAsJsonObject(module.name).get("selected").getAsBoolean();
                if (selected) {
                    Expensive.getInstance().styleManager.setCurrentStyle(module);
                }
                String[] color = colors.split(",");
                for (int i = 0; i < color.length; i++) {
                    module.colors = Arrays.copyOf(module.colors, color.length);
                    module.colors[i] = new Color(Integer.parseInt(color[i]));
                }
            }

        }

    }
}
