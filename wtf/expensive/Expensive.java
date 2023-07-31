package wtf.expensive;

import viamcp.ViaMCP;
import wtf.expensive.command.CommandManager;
import wtf.expensive.command.macro.MacroManager;
import wtf.expensive.config.ConfigManager;
import wtf.expensive.event.Event;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.EventProtocol;
import wtf.expensive.event.impl.player.EventMouseTick;
import wtf.expensive.friendSystem.FriendManager;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleManager;
import wtf.expensive.styles.StyleConfig;
import wtf.expensive.styles.StyleManager;
import wtf.expensive.ui.csgo.CSGui;

import wtf.expensive.utility.drag.DragManager;
import wtf.expensive.utility.drag.Dragging;
import wtf.expensive.utility.math.ScaleMath;

public class Expensive {
    public static final Expensive instance = new Expensive();
    public String VERSION = "000521 (b1)";
    private final ScaleMath scaleMath = new ScaleMath(2);
    public static Profile profile;

    private final EventProtocol<Event> eventProtocol = new EventProtocol<>();
    private ModuleManager moduleManager;
    public FriendManager friendManager;

    public CSGui csGui;
    public CommandManager commandManager;
    public MacroManager macroManager;
    public StyleManager styleManager;
    public ConfigManager config;

    public void init() {
        styleManager = new StyleManager();
        moduleManager = new ModuleManager();

        moduleManager.arraylist.sorted = moduleManager.getModules();
        csGui = new CSGui();
        commandManager = new CommandManager();
        try {

            config = new ConfigManager();

            macroManager = new MacroManager();
            macroManager.init();

            friendManager = new FriendManager();
            friendManager.init();

            StyleConfig.load();

        } catch (Exception e) {
            e.printStackTrace();
        }


        DragManager.load();

        ViaMCP.init();
        eventProtocol.register(this);
    }

    public void keyTyped(int key) {
        for (Module m : moduleManager.getModules()) {
            if (m.bind == key) {
                m.toggle();
            }
        }
        if (macroManager != null) {
            macroManager.onKeyPressed(key);
        }
    }

    private final EventListener<EventMouseTick> onTickMouse = event -> {

        for (Module m : moduleManager.getModules()) {
            if (m.mouseBind == event.getButton() && event.getButton() > 2) {
                m.toggle();
            }
        }
    };

    public static Expensive getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }


    public EventProtocol<Event> getEventProtocol() {
        return eventProtocol;
    }

    public Dragging createDrag(Module module, String name, float x, float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }

    public ScaleMath getScaleMath() {
        return scaleMath;
    }

}
