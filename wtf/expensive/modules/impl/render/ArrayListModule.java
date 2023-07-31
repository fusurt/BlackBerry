package wtf.expensive.modules.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.Event;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.drag.Drag;
import wtf.expensive.utility.drag.Dragging;
import wtf.expensive.utility.font.FontRenderer;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.List;

@ModuleAnnotation(name = "ArrayList", type = Type.RENDER)
public class ArrayListModule extends Module {

    public List<Module> sorted;


    public ModeSetting fonts = new ModeSetting("Fonts", "Rubik", "Rubik", "Ubuntu", "Greycliff", "Vag", "Roboto", "Minecraft", "Hack");
    public ModeSetting suffix = new ModeSetting("Suffix", "None", "~", "->", "/", "-", "$", "None");

    public BooleanSetting hide = new BooleanSetting("Hide Render", false);

    public SliderSetting alpha = new SliderSetting("Alpha", 255, 0, 255, 5);
    public SliderSetting height = new SliderSetting("Height", 14, 10, 20, 1f);
    public SliderSetting animationSpeed = new SliderSetting("Animation Speed", 15, 5, 20, 1f);
    public SliderSetting colorSpeed = new SliderSetting("Color Speed", 6, 1, 10, 1);
    public SliderSetting colorOffset = new SliderSetting("Color Offset", 2, 1, 10, 1);
    public BooleanSetting shadow = new BooleanSetting("Shadow", true);
    public BooleanSetting blur = new BooleanSetting("Blur", true);


    public FontRenderer getFont() {
        if (fonts.is("Rubik")) return Fonts.RUBIK_14;
        if (fonts.is("Ubuntu")) return Fonts.ubuntu14;
        if (fonts.is("Greycliff")) return Fonts.GREYCLIFF_14;
        if (fonts.is("Vag")) return Fonts.vag14;
        if (fonts.is("Roboto")) return Fonts.roboto14;
        if (fonts.is("Minecraft")) return Fonts.MCR14;
        if (fonts.is("Hack")) return Fonts.hack14;

        return Fonts.RUBIK_14;
    }

    boolean isFlip;

    public Dragging drag = Expensive.getInstance().createDrag(this, "list", 2, 20);

    public EventListener<EventDraw> onRender = e -> {
        setDisplayName(fonts.get());
        if (e.type == EventDraw.RenderType.DISPLAY) {


            float listY = drag.getY();

            float offset = 0;


            float xArray = drag.getX();
            float speed = animationSpeed.get();
            float height = this.height.get();

            GL11.glPushMatrix();
            float maxWidth = getSorted().stream().filter(m -> m.state).map(m -> getFont().getStringWidth(m.getDisplayName(false))).max(Float::compare).orElse((int) 0f);


            isFlip = xArray > e.sr.getScaledWidth() / 2f - maxWidth / 2f;
            drag.setWidth(maxWidth + 8);
            if (!isFlip) {

                for (Module module : getSorted()) {
                    if (hide.get() && module.category == Type.RENDER) continue;
                    float stringWidth = getFont().getStringWidth(module.getDisplayName(false)) + 8;

                    module.animation = AnimationMath.fast(module.animation, module.state ? 0 : stringWidth + 5, speed);
                    float listX = xArray - module.animation;
                    if (module.animation < stringWidth + 3) {
                        int alpha = (int) (this.alpha.get() - (int) (module.animation / (stringWidth + 3) * this.alpha.get()));
                        int alpha255 = (int) (255 - (int) (module.animation / (stringWidth + 3) * 255));
                        float finalOffset = offset;
                        if (blur.get() && alpha255 > 25) {
                            NORMAL_BLUR_RUNNABLES.add(() -> {
                                RenderUtility.drawRect(listX, listY + finalOffset, stringWidth, height, ColorUtility.rgba(21, 21, 21, 255));
                            });
                        }
                        if (shadow.get() && alpha255 > 25) {
                            NORMAL_SHADOW_BLACK.add(() -> {
                                RenderUtility.drawRect(listX, listY + finalOffset, stringWidth, height, ColorUtility.rgba(21, 21, 21, 50));
                            });
                        }
                        //левый арралист
                        GlowUtility.drawGlowGradient(listX + 2, listY + offset, stringWidth, height, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                        GlowUtility.drawGlowGradient(listX + 2, listY + offset, stringWidth, height, 15, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
                        if (alpha255 > 25)
                            getFont().drawStringWithShadow(module.getDisplayName(false), listX + 5, listY + offset + height / 2 - 1, -1);
                        offset += (height * (1 - module.animation / (stringWidth + 3)));
                    }
                }
            } else {
                for (Module module : getSorted()) {
                    if (hide.get() && module.category == Type.RENDER) continue;

                    float stringWidth = getFont().getStringWidth(module.getDisplayName(false)) + 8;

                    module.animation = AnimationMath.fast(module.animation, module.state ? 0 : stringWidth + 5, speed);
                    float listX = xArray + module.animation + maxWidth + 8;
                    if (module.animation < stringWidth + 3) {
                        int alpha = (int) (this.alpha.get() - (int) (module.animation / (stringWidth + 3) * this.alpha.get()));
                        int alpha255 = (int) (255 - (int) (module.animation / (stringWidth + 3) * 255));
                        float finalOffset = offset;
                        if (blur.get() && alpha255 > 25) {
                            NORMAL_BLUR_RUNNABLES.add(() -> {
                                RenderUtility.drawRect(listX - stringWidth, listY + finalOffset, stringWidth, height, ColorUtility.rgba(21, 21, 21, 255));
                            });
                        }
                        if (shadow.get() && alpha255 > 25) {
                            NORMAL_SHADOW_BLACK.add(() -> {
                                RenderUtility.drawRect(listX - stringWidth, listY + finalOffset, stringWidth, height, ColorUtility.rgba(21, 21, 21, 50));
                            });
                        }
                        //правый арралист
                        GlowUtility.drawGlowGradient(listX - 1 - stringWidth, listY + offset, stringWidth, height, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                        GlowUtility.drawGlowGradient(listX - 1 - stringWidth, listY + offset, stringWidth, height, 15, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
                        if (alpha255 > 25)
                            getFont().drawStringWithShadow(module.getDisplayName(false), listX - stringWidth + 3f, listY + offset + height / 2 - 1, -1);
                        offset += (height * (1 - module.animation / (stringWidth + 3)));
                    }
                }
            }

            drag.setHeight(offset);
            GL11.glPopMatrix();

        }
    };

    public int getColor(float index) {
        return Expensive.getInstance().styleManager.getCurrentStyle().getColor((int) index);
    }
    public int getColor(float index, float alphaPC) {
        final int color = Expensive.getInstance().styleManager.getCurrentStyle().getColor((int) index);
        return ColorUtility.setAlpha(color,(int)((float)ColorUtility.getAlpha(color)*alphaPC));
    }

    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    public List<Module> getSorted() {
        List<Module> modules = Expensive.getInstance().getModuleManager().getModules();
        modules.sort((o1, o2) -> {
            float width1 = getFont().getStringWidth(o1.getDisplayName(isFlip)) + 4;
            float width2 = getFont().getStringWidth(o2.getDisplayName(isFlip)) + 4;
            return Float.compare(width2, width1);
        });
        return modules;
    }

}
