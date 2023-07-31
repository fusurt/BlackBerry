package wtf.expensive.ui.csgo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wtf.expensive.Expensive;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.render.ClickGuiModule;
import wtf.expensive.ui.csgo.elements.ModuleElement;
import wtf.expensive.ui.csgo.elements.StyleElement;
import wtf.expensive.utility.animations.Animation;
import wtf.expensive.utility.animations.Direction;
import wtf.expensive.utility.animations.impl.DecelerateAnimation;
import wtf.expensive.utility.animations.impl.EaseBackIn;
import wtf.expensive.utility.animations.impl.EaseInOutQuad;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.GaussianBlur;
import wtf.expensive.utility.shader.StencilUtil;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSGui extends GuiScreen {

    public float x, y;
    public List<StyleElement> styles = new ArrayList<>();
    public List<StyleElement> styles2 = new ArrayList<>();

    public CSGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        x = sr.getScaledWidth() / 2 - width / 2 + 70;
        y = sr.getScaledHeight() / 2 - height / 2;
        Expensive.getInstance().getModuleManager().getModules().forEach(module -> {
            if (Expensive.getInstance().getModuleManager().getModules().indexOf(module) % 2 != 0) {
                elements.add(new ModuleElement(module));
            }
            if (Expensive.getInstance().getModuleManager().getModules().indexOf(module) % 2 == 0) {
                elements2.add(new ModuleElement(module));
            }
        });


        Expensive.getInstance().styleManager.styles.forEach(module -> {
            if (Expensive.getInstance().styleManager.styles.indexOf(module) % 2 == 0) {
                styles.add(new StyleElement(module));
            }
            if (Expensive.getInstance().styleManager.styles.indexOf(module) % 2 != 0) {
                styles2.add(new StyleElement(module));
            }
        });


    }

    public float animation;
    public float renderAnimation;

    public Type selected;

    public float scrollY = 10, scrollY2 = 10;

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (selected == Type.THEME) {
            for (StyleElement style : styles) {
                style.mouseReleased(mouseX, mouseY, state);
            }
            for (StyleElement style : styles2) {
                style.mouseReleased(mouseX, mouseY, state);
            }
            dragging = false;
            return;
        }
        for (ModuleElement parameter : elements) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.mouseReleased(mouseX, mouseY, state);
        }
        for (ModuleElement parameter : elements2) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.mouseReleased(mouseX, mouseY, state);
        }
        dragging = false;
    }

    public List<ModuleElement> elements = new ArrayList<>();

    public List<ModuleElement> elements2 = new ArrayList<>();
    public int prevMouseX, prevMouseY;

    public Animation anim;
    float scale;
    public boolean close;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (close) {

            boolean ok = anim.isDone();
            if (ClickGuiModule.guiAnimations.get()) {
                if (ok) {

                    mc.displayGuiScreen(null);
                    close = false;
                }
            } else {

                mc.displayGuiScreen(null);
                close = false;
            }

        }

        updateMouse(mouseX, mouseY);

        float width = 400;
        float height = 220;
        scale = (float) anim.getOutput();
        GlStateManager.pushMatrix();

        if (ClickGuiModule.guiAnimations.get()) {
            GlStateManager.translate(x + (width / 2), (y + height + 50) / 2, 0);
            GlStateManager.scale(scale, scale, 0);
            GlStateManager.translate(-(x + (width / 2)), -((y + height + 50) / 2), 0);
        }

        if (anim.isDone())

            GaussianBlur.renderBlur(50, () -> RiseShaders.RQ.draw(x, y, width, 220, 8, new Color(21, 21, 25, 180)));
        StencilUtil.initStencilToWrite();
         RiseShaders.RQ.draw(x, y, width, height, 7, new Color(21, 21, 25, 180));
        StencilUtil.readStencilBuffer(1);
        RiseShaders.RQ.draw(x + 4, y + 4, 85, 212, 8, new Color(0, 0, 0, 60));
        RiseShaders.RQ.draw(x + 4, y + 189, 85, 28, 7, new Color(0, 0, 0, 132));
        RiseShaders.RQ.draw(x, y, width, height, 8, new Color(13, 13, 17, 180));
        renderAnimation = AnimationMath.fast(renderAnimation, animation, 5f);
        animation = 1;
        animation = MathHelper.clamp(animation, 0, 1);

        float y = this.y;

        for (Type type : Type.values()) {


            float offset = 25;
            if (type == selected) {
             //   RiseShaders.RQ.draw(x + 4f, y + 9.5f + type.ordinal() * offset, 92, 15, 3, new Color(255, 255, 255, 25));
                RenderUtility.drawGradientHorizontal(x + 7.2f, y + 7.5f + type.ordinal() * offset, 18, 18, 3, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), 255).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), 255).getRGB());
            }

            Fonts.SEMI_BOLD_16.drawString(type.name(), x + 30f, y + 15 + type.ordinal() * offset, -1);
        }
        RenderUtility.drawImage(new ResourceLocation("expensive/images/combat" + ".png"), x + 10f, y + 11, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/movement" + ".png"), x + 10f, y + 36, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/render" + ".png"), x + 10f, y + 60.5f, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/player" + ".png"), x + 10f, y + 85.0f, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/other" + ".png"), x + 10f, y + 110.5f, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/theme" + ".png"), x + 10f, y + 135.5f, 13, 13, Color.WHITE, 1);
        RenderUtility.drawImage(new ResourceLocation("expensive/images/ava" + ".png"), x + 8, y + 192, 22, 22, Color.WHITE, 1);
         Fonts.MONTSERRAT16.drawString("User:", x + 32, y + 197, Color.GRAY.getRGB());
        Fonts.MONTSERRAT16.drawString("Fusurt", x + 56, y + 197, -1);
        Fonts.MONTSERRAT16.drawString("Role:", x + 32, y + 207, Color.GRAY.getRGB());
        Fonts.MONTSERRAT16.drawString("Dev", x + 56, y + 207, -1);

        drawingComponents(mouseX, mouseY);

        StencilUtil.uninitStencilBuffer();
        GlStateManager.popMatrix();

    }

    public void drawingComponents(int mouseX, int mouseY) {
        scrollY += Mouse.getDWheel() / 15f;
        float offset = scrollY;
        if (selected == Type.THEME) {
            for (StyleElement style : styles) {
                style.x = x + 110;
                style.y = AnimationMath.fast(style.y, y + offset, 15f);
                style.width = 135;
                style.height = 16;
                style.draw(mouseX, mouseY);
                offset += style.height + 4;
            }
                offset = scrollY;
            for (StyleElement style : styles2) {
                style.x = x + 255;
                style.y = AnimationMath.fast(style.y, y + offset, 15f);
                style.width = 135;
                style.height = 16;
                style.draw(mouseX, mouseY);
                offset += style.height + 4;
            }
            float offsetMax = 0;

            float offset1 = 10;
            for (StyleElement parameter : styles) {
                offset1 += parameter.height + 4;
            }

            float offset2 = 10;
            for (StyleElement parameter : styles2) {
                offset2 += parameter.height + 4;
            }
            offsetMax = Math.max(offset1, offset2);
            if (offsetMax > 300) {
                scrollY = MathHelper.clamp(scrollY, -offsetMax + 300, 10);
            } else
                scrollY = 10;
            return;
        }
        List<ModuleElement> e = this.elements.stream().filter(moduleElement -> moduleElement.module.category == selected).toList();
        for (ModuleElement parameter : e) {
            parameter.x = x + 110;
            parameter.y = AnimationMath.fast(parameter.y, y + offset, 15f);
            parameter.width = 135;
            if (parameter.extended) {
                for (Element element : parameter.elements) {
                    if (!element.isVisible()) continue;
                    offset += element.height;
                }
            }
            parameter.height = 16;
            parameter.draw(mouseX, mouseY);
            offset += parameter.height + 4;
        }
        List<ModuleElement> e2 = this.elements2.stream().filter(moduleElement -> moduleElement.module.category == selected).toList();
        offset = scrollY;
        for (ModuleElement parameter : e2) {
            parameter.x = x + 255;
            parameter.y = AnimationMath.fast(parameter.y, y + offset, 15f);
            parameter.width = 135;
            if (parameter.extended) {
                for (Element element : parameter.elements) {
                    if (!element.isVisible()) continue;
                    offset += element.height;
                }
            }
            parameter.height = 16;
            parameter.draw(mouseX, mouseY);
            offset += parameter.height + 4;
        }

        float offsetMax = 0;

        float offset1 = 10;
        for (ModuleElement parameter : elements) {
            if (parameter.module.category != selected) {
                continue;
            }
            if (parameter.extended) {
                for (Element element : parameter.elements) {
                    offset1 += element.height;
                }
            }
            offset1 += parameter.height + 4;
        }

        float offset2 = 10;
        for (ModuleElement parameter : elements2) {
            if (parameter.module.category != selected) {
                continue;
            }
            if (parameter.extended) {
                for (Element element : parameter.elements) {
                    offset2 += element.height;
                }
            }
            offset2 += parameter.height + 4;
        }
        offsetMax = Math.max(offset1, offset2);
        if (offsetMax > 200) {
            scrollY = MathHelper.clamp(scrollY, -offsetMax + 200, 10);
        } else
            scrollY = 10;


    }

    @Override
    public void initGui() {
        anim = new EaseInOutQuad(400, 1, Direction.FORWARDS);
        anim.setDirection(Direction.FORWARDS);
        anim.setDuration(350);

        super.initGui();
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        for (ModuleElement parameter : elements) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.keyTyped(typedChar, keyCode);
        }
        for (ModuleElement parameter : elements2) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.keyTyped(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            anim.setDirection(Direction.BACKWARDS);
            anim.setDuration(250);
            close = true;
        }

        //    super.keyTyped(typedChar, keyCode);
    }

    public boolean dragging = false;

    public void updateMouse(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX + prevMouseX;
            y = mouseY + prevMouseY;
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (selected == Type.THEME) {
            for (StyleElement style : styles) {
                style.mouseClicked(mouseX, mouseY, mouseButton);
            }
            for (StyleElement style : styles2) {
                style.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if (mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + 340) {
                float y = this.y;
                for (Type type : Type.values()) {
                    float offset = 25;
                    if (HoveringMath.isHovering(x + 4f, y + 9.5f + type.ordinal() * offset, 92, 20, mouseX, mouseY) && type != selected) {
                        selected = type;
                        renderAnimation = 0;
                        animation = 0;
                    }
                }
            }
            if (HoveringMath.isHovering(x, y - 20, width, 25, mouseX, mouseY)) {
                prevMouseX = (int) (x - mouseX);
                prevMouseY = (int) (y - mouseY);
                dragging = true;
            }
            return;
        }
        if (HoveringMath.isHovering(x, y - 20, width, 25, mouseX, mouseY)) {
            prevMouseX = (int) (x - mouseX);
            prevMouseY = (int) (y - mouseY);
            dragging = true;
        }

        for (ModuleElement parameter : elements) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for (ModuleElement parameter : elements2) {
            if (parameter.module.category != selected) {
                continue;
            }
            parameter.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + 340) {
            float y = this.y;
            for (Type type : Type.values()) {
                float offset = 25;
                if (HoveringMath.isHovering(x + 4f, y + 9.5f + type.ordinal() * offset, 92, 20, mouseX, mouseY) && type != selected) {
                    selected = type;
                    renderAnimation = 0;
                    animation = 0;
                }
            }
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
