package wtf.expensive.modules.impl.render;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.animations.Animation;
import wtf.expensive.utility.animations.Direction;
import wtf.expensive.utility.animations.impl.DecelerateAnimation;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(name = "Notifications", desc = "���������� ����������� �� ������", type = Type.RENDER)
public class Notifications extends Module {


    public static List<Notify> notifies = new ArrayList<>();

    public static void notify(String title, String text, Notify.NotifyType type, int second) {
        notifies.add(new Notify(title, text, type).setMaxTime(second * 50));
    }


    private final EventListener<EventUpdate> update = e -> {
        notifies.forEach(Notify::updateAlpha);
        notifies.removeIf(Notify::updateAlpha);

        if (notifies.size() > Expensive.getInstance().getModuleManager().getModules().size() - 1 || notifies.size() > 25) {
            notifies.clear();
        }
    };

    public static void render(ScaledResolution res) {
        float yOffset = -24;
        if (mc.currentScreen instanceof GuiChat) {
            int i = mc.gameSettings.guiScale;
            if (i == 0) {
                yOffset -= 26;
            }
            if (i == 2) {
                yOffset -= 14;
            }
        }
        for (Notify notify : notifies) {
            GL11.glPushMatrix();
            yOffset -= notify.draw(res, yOffset);
            GL11.glPopMatrix();
        }
    }

    public static class Notify {
        private String title, text;
        private float width;
        private int ticks, maxTime = 50;
        private final NotifyType type;

        private float y = 0;
        public int alpha = 0;
        Animation downAnimation;

        public Notify(String title, String text, NotifyType type) {
            this.title = title;
            this.text = text;
            this.type = type;
            this.width = Math.max(Fonts.SEMI_BOLD_12.getStringWidth(title), Fonts.SEMI_BOLD_12.getStringWidth(text));
        }

        public float draw(ScaledResolution res, float yOffset) {
            float alphaSetting = Expensive.getInstance().getModuleManager().arraylist.alpha.get();

            downAnimation = new DecelerateAnimation(225, 1);

            downAnimation.setDirection(ticks < 50 ? Direction.FORWARDS : Direction.BACKWARDS);

            y = AnimationMath.fast(y, yOffset, 15);
            int w = (int) Expensive.getInstance().getScaleMath().calc(res.getScaledWidth()),
                    h = Expensive.getInstance().getScaleMath().calc(res.getScaledHeight());
            Expensive.getInstance().getScaleMath().pushScale();
            if (alpha > 10) {
                int color = Expensive.getInstance().getModuleManager().arraylist.getColor(5);
                if (alpha > 200)
                    NORMAL_BLUR_RUNNABLES.add(() -> RenderUtility.drawRound(w - width - 30, h + y, width + 27,
                            21f, 2, ColorUtility.rgba(20, 20, 20, alpha)));
                NORMAL_SHADOW_BLACK.add(() -> RenderUtility.drawRound(w - width - 30, h + y, width + 27,
                        21f, 2, ColorUtility.rgba(20, 20, 20, MathHelper.clamp(alpha, 0, 50))));

                GlowUtility.drawGlowHorizontal(w - width - 30, h + y, width + 27, 21f, 15, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), alpha).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), alpha).getRGB());
                //RenderUtility.drawRound(w - width - 30, h + y, width + 27, 21f, 2, ColorUtility.rgba(20, 20, 20, MathHelper.clamp(alpha, 0, (int) alphaSetting)));
                if (type == NotifyType.ERROR) {
                    RenderUtility.drawImage(new ResourceLocation("expensive/images/notify/error.png"), w - width - 26, h + y + 3,
                            15, 15, new Color(ColorUtility.rgba(200, 200, 200, 255)), alpha / 255.0f);
                } else if (type == NotifyType.INFORMATION) {
                    RenderUtility.drawImage(new ResourceLocation("expensive/images/notify/info.png"), w - width - 26, h + y + 3,
                            15, 15, new Color(ColorUtility.rgba(200, 200, 200, 255)), alpha / 255.0f);
                } else if (type == NotifyType.SUCCESS) {
                    RenderUtility.drawImage(new ResourceLocation("expensive/images/notify/success.png"), w - width - 26, h + y + 3,
                            15, 15, new Color(ColorUtility.rgba(200, 200, 200, 255)), alpha / 255.0f);
                }
                Fonts.SEMI_BOLD_14.drawString(this.title, w - width - 6, h + 5 + y, ColorUtility.rgba(200, 200, 200, MathHelper.clamp(alpha, 10, 255)));
                Fonts.SEMI_BOLD_12.drawString(this.text, w - width - 6, h + 14 + y, ColorUtility.rgba(170, 170, 170, MathHelper.clamp(alpha, 10, 255)))
                ;
            }
            Expensive.getInstance().getScaleMath().popScale();

            return 24;
        }

        public enum NotifyType {
            SUCCESS, INFORMATION, ERROR
        }

        public boolean updateAlpha() {
            alpha = (int) MathUtility.clamp(alpha, 0.0F, 255.0F);
            if (++ticks < maxTime) {
                alpha = (int) AnimationMath.animation((float) alpha, 255, (float) (AnimationMath.deltaTime() * 10));
            } else {
                alpha = (int) AnimationMath.animation((float) alpha, (float) 0, (float) (AnimationMath.deltaTime() * 10));
            }
            return alpha == 0;
        }

        public Notify setMaxTime(int maxTime) {
            this.maxTime = maxTime;
            return this;
        }
    }
}