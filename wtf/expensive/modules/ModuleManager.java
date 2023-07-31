package wtf.expensive.modules;

import wtf.expensive.modules.impl.combat.*;
import wtf.expensive.modules.impl.movement.*;
import wtf.expensive.modules.impl.movement.NoJumpDelay;
import wtf.expensive.modules.impl.player.*;
import wtf.expensive.modules.impl.render.*;
import wtf.expensive.modules.impl.util.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();
    public final ClickGuiModule clickGuiModule;
    public final WorldModule world;
    public final ArrayListModule arraylist;
    public final BackTrackModule backTrackModule;

    public ModuleManager() {
        // Hud
        registerModule(clickGuiModule = new ClickGuiModule());
        registerModule(world = new WorldModule());
        registerModule(arraylist = new ArrayListModule());
        registerModule(new BlockESPModule());
        registerModule(new EntityESPModule());
        registerModule(new FullBrightModule());
        registerModule(new ItemESPModule());
        registerModule(new Notifications());
        registerModule(new HUDModule());
        registerModule(new KeybindsModule());
        registerModule(new ShaderESP());
        registerModule(new JumpCircle());
        registerModule(new NameProtect());
        registerModule(new NoRenderModule());
        registerModule(new PearlPredictionModule());
        registerModule(new SwingAnimationModule());
        registerModule(new TracersModule());
        registerModule(new TrailsModule());
        // Combat
        registerModule(new AuraModule());
        registerModule(new AutoTotem());
        registerModule(new GAppleCooldown());
        registerModule(new HitBoxModule());
        registerModule(new KeepSprintModule());
        registerModule(new ShieldBreaker());
        registerModule(new TargetStrafeModule());
        registerModule(new TPAura());
        registerModule(new VelocityModule());
        // Movement
        registerModule(new AutoSprintModule());
        registerModule(new AirJump());
        registerModule(new DamageSpeedModule());
        registerModule(new ElytraFly());
        registerModule(new FlightModule());
        registerModule(new Jesus());
        registerModule(new NoSlowModule());
        registerModule(new SpeedModule());
        registerModule(new Spider());
        registerModule(new StrafeModule());
        registerModule(new TimerModule());
        registerModule(new WaterSpeedModule());
        // Player
        registerModule(new AntiLevitation());
        registerModule(new AutoPotionModule());
        registerModule(backTrackModule = new BackTrackModule());
        registerModule(new FastBreak());
        registerModule(new FreeCam());
        registerModule(new InventoryMoveModule());
        registerModule(new ItemScroller());
        registerModule(new NoClip());
        registerModule(new AutoRespawn());
        registerModule(new DeathCoords());
        registerModule(new AutoTPAccept());
        registerModule(new NoInteractModule());
        registerModule(new NoJumpDelay());
        registerModule(new NoPush());
        registerModule(new NoWeb());
        registerModule(new StaffStatisticModule());

        // Util
        registerModule(new AntiAFK());
        registerModule(new AntiThrowBlock());
        registerModule(new ClickPearl());
        registerModule(new AirDropSteallerModule());
        registerModule(new Disabler());
        registerModule(new ElytraFix());
        registerModule(new GriefJoiner());
        registerModule(new InventorySync());
        registerModule(new NoServerRotation());
        registerModule(new OptimizationModule());

    }


    public void registerModule(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }


    public Module get(Class<? extends Module> classModule) {
        for (Module module : modules) {
            if (module != null && module.getClass() == classModule) {
                return module;
            }
        }
        return null;
    }

    public Module get(String name) {
        for (Module module : modules) {
            if (module != null && module.name.equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

}
