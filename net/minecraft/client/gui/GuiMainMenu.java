package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.CustomPanorama;
import optifine.CustomPanoramaProperties;
import optifine.Reflector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import wtf.expensive.ui.CustomButton;
import wtf.expensive.ui.buttons.*;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.HoveringMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.GaussianBlur;
import wtf.expensive.utility.shader.StencilUtil;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;
import wtf.expensive.utility.shader.wtf.shader.impl.RQShader;

public class GuiMainMenu extends GuiScreen {

    public boolean alt;

    public boolean printing;
    public String print = "";
    float animation;
    float animationPresets;
    boolean isPreset;
    public float blur;
    public int lastX;
    public int lastY;

    public List<CustomButton> buttons = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
        buttons.clear();
        buttons.add(new SingleButton());
        buttons.add(new MultiButton());
        buttons.add(new AltButton());
        buttons.add(new OptionsButton());
        buttons.add(new ExitButton());


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtility.drawImage(new ResourceLocation("wall.jpg"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.WHITE, 1);
        float buttonWidth = 100;
        float buttonHeight = 15;

        float x = sr.getScaledWidth() / 2f - buttonWidth / 2;
        float y = sr.getScaledHeight() / 2f - buttonHeight / 2;
        float heightOffsrt = 0;
        for (CustomButton button : buttons) {
            heightOffsrt += button.height + 2;
        }
        float finalHeightOffsrt = heightOffsrt;
        y -= (heightOffsrt - buttonHeight - 8) / 2;
        float finalY = y;

        float finalY1 = y;
        if (HoveringMath.isHovering(x - 10, y - 20, buttonWidth + 20, buttonHeight + heightOffsrt + 17, mouseX, mouseY)) {
            animationPresets = 0.2f;
        } else {
            animationPresets = 0;
        }
        RiseShaders.RQ.draw(x - 10, y - 20, buttonWidth + 20, buttonHeight + heightOffsrt + 17, 5, new Color(22, 22, 22, 201));
        float offset = 0;
        for (CustomButton button : buttons) {
            button.x = x;
            button.y = y + offset;
            button.width = buttonWidth;

            button.draw(mouseX, mouseY);
            offset += button.height + 2;
        }

        Fonts.vag14.drawCenteredString("blackberry release", sr.getScaledWidth() / 2f, y - 12, new Color(255, 255, 255, 255).getRGB());
        lastX = mouseX;
        lastY = mouseY;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (CustomButton button : buttons) {
            if (button.isHovered(mouseX, mouseY))
                button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (CustomButton button : buttons) {
            button.keyTyped(typedChar, keyCode);
        }
    }


}
