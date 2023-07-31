package wtf.expensive.modules.impl.render;

import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.input.Keyboard;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.OptimizationModule;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.drag.Dragging;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "Keybinds", type = Type.RENDER)
public class KeybindsModule extends Module {
    Dragging drag = Expensive.getInstance().createDrag(this, "keybinds", 2, 20);
    int alphaText;
    int alpha;
    public EventListener<EventDraw> onRender = e -> {
        if (e.type == EventDraw.RenderType.DISPLAY) {
            alpha = (int) AnimationMath.fast(alpha, Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).toList().size() > 0 ? 250 : 0, 15f);
            drag.setWidth(70);
            if (alpha < 5) return;
            if (alpha > 100) {
                                }
                        NORMAL_BLUR_RUNNABLES.add(() -> {
                            RenderUtility.drawRect(drag.getX(), drag.getY() + 10, 70, 8 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), new Color(10, 10, 10, alpha).getRGB());
            });
            NORMAL_SHADOW_BLACK.add(() -> {
                RenderUtility.drawRect(drag.getX(), drag.getY(), 70, 10, new Color(10, 10, 10, alpha).getRGB());
                RenderUtility.drawRect(drag.getX(), drag.getY(), 70, 1, new Color(109, 253, 176, alpha).getRGB());
                RenderUtility.drawRect(drag.getX(), drag.getY() + 10, 70, 8 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), new Color(10, 10, 10, alpha).getRGB());
            });
            //глоу под названием KeyBinds
            GlowUtility.drawGlowGradient(drag.getX() - 5f, drag.getY() - 5.5f, 80, 13.5f, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
            GlowUtility.drawGlowGradient(drag.getX() - 5f, drag.getY() - 5.5f, 80, 13.5f, 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
            //название над глоу
            Fonts.vag14.drawStringWithShadow("KeyBinds", drag.getX() + 40.0f / 2f, drag.getY(), new Color(255, 255, 255, alpha).getRGB());
            //
            GlowUtility.drawGlowGradient(drag.getX() - 5, drag.getY() + 10, 80, 9 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
            GlowUtility.drawGlowGradient (drag.getX() - 5, drag.getY() + 10, 80, 9 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
            double i = 8.5;
            for (Module m : Expensive.getInstance().getModuleManager().getModules()) {
                if (m.bind > 0 && m.state) {
                    Fonts.vag14.drawStringWithShadow(m.name, drag.getX() + 2, (drag.getY() + 10 / 2f) + i, new Color(255, 255, 255, alpha).getRGB());
                    Fonts.vag14.drawCenteredStringWithShadow("[" + Keyboard.getKeyName(m.bind) + "]", drag.getX() + 82 / 2f + 23, (drag.getY() + 9 / 2f) + i, new Color(255, 255, 255, alpha).getRGB());
                    drag.setHeight((float) (10 + i));
                    i += 8;
            OptimizationModule optimizationModule = (OptimizationModule) Expensive.getInstance().getModuleManager().get(OptimizationModule.class);
            if ((optimizationModule != null && optimizationModule.state && optimizationModule.shaders.get()));
                //глоу под бинжеными функциями
            //GlowUtility.drawGlowGradient(drag.getX() - 5, drag.getY() + 10, 80, 9 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
            //GlowUtility.drawGlowGradient (drag.getX() - 5, drag.getY() + 10, 80, 9 * Expensive.getInstance().getModuleManager().getModules().stream().filter(module -> module.state).filter(module -> module.bind > 0).collect(Collectors.toList()).size(), 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
            //double i = 8.5;
            //for (Module m : Expensive.getInstance().getModuleManager().getModules()) {
                //if (m.bind > 0 && m.state) {
                    //Fonts.vag14.drawStringWithShadow(m.name, drag.getX() + 2, (drag.getY() + 10 / 2f) + i, new Color(255, 255, 255, alpha).getRGB());
                    //Fonts.vag14.drawCenteredStringWithShadow("[" + Keyboard.getKeyName(m.bind) + "]", drag.getX() + 82 / 2f + 23, (drag.getY() + 9 / 2f) + i, new Color(255, 255, 255, alpha).getRGB());
                    //drag.setHeight((float) (10 + i));
                   // i += 8;
                }
            }
        }
    };
}
