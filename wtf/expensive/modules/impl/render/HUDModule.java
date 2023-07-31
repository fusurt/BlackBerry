package wtf.expensive.modules.impl.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.command.impl.CommandNameProtect;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraModule;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.utility.animations.Animation;
import wtf.expensive.utility.animations.Direction;
import wtf.expensive.utility.animations.impl.DecelerateAnimation;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.drag.Dragging;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "HUD", type = Type.RENDER)
public class HUDModule extends Module {


    public BooleanSetting title = new BooleanSetting("Title", true);
    public BooleanSetting showCoords = new BooleanSetting("Show Coordinates", true);
    public BooleanSetting showPing = new BooleanSetting("Show Ping", true);
    public BooleanSetting showFPS = new BooleanSetting("Show FPS", true);
    public BooleanSetting showBPS = new BooleanSetting("Show BPS", true);
    public static BooleanSetting showPotionStatus = new BooleanSetting("Show Potion Status", true);
    public BooleanSetting showArmorHUD = new BooleanSetting("Show ArmorHUD", true);

    public BooleanSetting showTarget = new BooleanSetting("Show TargetHUD", true);

    public static BooleanSetting blur = new BooleanSetting("Blur", true);

    public static BooleanSetting shadow = new BooleanSetting("Shadow", true);

    float yAnimation = 0;
    public float rotation = 0;
    float hp, ar;
    int posX, posY, alpha = 0;
    private EntityLivingBase curTarget = null;

    public final Dragging targetHUDDrag = Expensive.getInstance().createDrag(this, "targetHUD", 282, 266);
    public final Dragging potionStatusDrag = Expensive.getInstance().createDrag(this, "potions", 856, 32);
    Animation animation = new DecelerateAnimation(200, 255, Direction.FORWARDS);

    private final EventListener<EventDraw> onRender = e -> {

        if (e.type == EventDraw.RenderType.DISPLAY) {

            if (title.get()) {
                int x = 2;
                int y = 4;
                GlowUtility.drawGlowGradient(5, 5, 120, 20, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                GlowUtility.drawGlowGradient(5, 5, 120, 20, 25, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
                RenderUtility.drawFadeString("BLACKBERRY", x + 7.5F, y + 5.5F);
            }

            yAnimation = AnimationMath.fast(yAnimation, mc.currentScreen instanceof GuiChat ?
                    Expensive.getInstance().getScaleMath().calc(14) : 0, 20);

            final float[] pos = {4, e.sr.getScaledHeight() - 15 - (int) yAnimation};

            List<String> strings = new ArrayList<>();

            if (showCoords.get())
                strings.add("XYZ: " + (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ);
            if (showFPS.get()) strings.add("FPS: " + Minecraft.getDebugFPS());
            if (showBPS.get()) strings.add("BPS: " + calculateBPS());
            if (showPing.get()) strings.add("PING: " + (mc.isSingleplayer() ? 0 : calculatePing()) + "ms");


            float x = 0;

            for (String s : strings) {

                float finalX = x;
                if (shadow.get())
                    NORMAL_SHADOW_BLACK.add(() -> RenderUtility.drawRect(pos[0] + finalX, pos[1],
                            Fonts.vag14.getStringWidth(s) + 4, 12f, ColorUtility.rgba(21, 21, 21, 50)));
                if (blur.get())
                    NORMAL_BLUR_RUNNABLES.add(() -> RenderUtility.drawRect(pos[0] + finalX, pos[1],
                            Fonts.vag14.getStringWidth(s) + 4, 12f, ColorUtility.rgba(21, 21, 21, 50)));
                GlowUtility.drawGlowGradient((pos[0] + x), pos[1], Fonts.vag14.getStringWidth(s) + 4, 12f, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                GlowUtility.drawGlowGradient((pos[0] + x), pos[1], Fonts.vag14.getStringWidth(s) + 4, 12f, 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));

                Fonts.vag14.drawString(s, pos[0] + x + 2, pos[1] + 5f, Color.WHITE.getRGB());


                x += Fonts.vag14.getStringWidth(s) + 10;
            }

            alpha = (int) MathUtility.clamp(alpha, 0, 255);

            if (AuraModule.instance.target != null) {
                curTarget = AuraModule.instance.target;
            } else if (mc.currentScreen instanceof GuiChat) {
                curTarget = mc.player;
            }

            if (showTarget.get() && curTarget != null) {


                Expensive.getInstance().getScaleMath().pushScale();

                float alphaSetting = Expensive.getInstance().getModuleManager().arraylist.alpha.get();

                boolean isNameProtect = Expensive.getInstance().getModuleManager().get(NameProtect.class).state;
                String protectName = CommandNameProtect.canChange ? CommandNameProtect.current : "Protected";

                String name = isNameProtect && curTarget instanceof EntityPlayerSP ? protectName : isNameProtect && NameProtect.youtuber.get() && curTarget instanceof EntityPlayer ? "Protected" : ChatFormatting.stripFormatting(curTarget.getName());
                int textWidth = Fonts.MONTSERRAT16.getStringWidth(name);
                int centerX = posX + 45 + (100 - textWidth) / 2;
                float width = Math.max(Fonts.SEMI_BOLD_14.getStringWidth(isNameProtect ? protectName : ChatFormatting.stripFormatting(curTarget.getName())) + 50, 100 + (25 / 2));
                targetHUDDrag.setWidth(width);
                targetHUDDrag.setHeight(35.5f);

                posX = (int) targetHUDDrag.getX();
                posY = (int) targetHUDDrag.getY();
                float x2 = posX;

                if (AuraModule.instance.target != null || mc.currentScreen instanceof GuiChat
                        && mc.player != null) {

                    animation.setDuration(300);
                    animation.setDirection(Direction.FORWARDS);
                } else {
                    animation.setDuration(300);
                    animation.setDirection(Direction.BACKWARDS);
                }

                hp = MathUtility.clamp(MathUtility.lerp(hp, curTarget.getHealth() / curTarget.getMaxHealth(), (float) (12 * AnimationMath.deltaTime())), 0, 1);
                //фон тхуда
                GlowUtility.drawGlowGradient(posX + 42.5F, posY - 16.5F, width + 11.5F,46.5f, 2, ColorUtility.applyOpacity(new Color(255, 255, 255), alpha).getRGB(), ColorUtility.applyOpacity(new Color(255, 255, 255), alpha).getRGB(), ColorUtility.applyOpacity(new Color(255, 255, 255), alpha).getRGB(), ColorUtility.applyOpacity(new Color(255, 255, 255), alpha).getRGB());
                GlowUtility.drawGlowHorizontal(posX + 41.5f, posY - 15.5f, (width + 10.5f), 45, 25, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), alpha).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), alpha).getRGB());
                alpha = (int) animation.getOutput();
                //чёрная тень под хпшками
                GlowUtility.drawGlowGradient(posX + 98, posY + 9, width - 60,8.5f, 10, ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB());
                //полоска хп тхуда
                RenderUtility.drawGradientHorizontal(posX + 100, posY + 10, (width - 62.5f) * hp, 6, 0, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), alpha).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), alpha).getRGB());
                if (curTarget instanceof EntityPlayer)
                    //бошка перса тхуда 1 тхуда
                GlowUtility.drawGlowGradient(posX + 49.5f, posY - 9,32,32, 7, ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB(), ColorUtility.applyOpacity(new Color(43, 51, 53), alpha).getRGB());
                RenderUtility.drawFace(alpha, posX + 49.5f, posY - 9, 8, 8, 8, 8, 32, 32, 64, 64, (AbstractClientPlayer) curTarget);
                if (!(curTarget instanceof EntityPlayer) && curTarget != null) {
                }
                if (alpha > 80) {
                    if (curTarget instanceof EntityPlayer);
                    //ник перса в тхуде 1 тхуда
                    Fonts.MONTSERRAT16.drawStringWithShadow(name, centerX + 30, posY - 3.0f, ColorUtility.rgba(200, 200, 200, MathHelper.clamp(alpha, 10, 255)));
                }
                Expensive.getInstance().getScaleMath().popScale();

            }
            if (showPotionStatus.get()) {
                ScaledResolution res = new ScaledResolution(mc);
                List<PotionEffect> effects = new ArrayList<>(mc.player.getActivePotionEffects());

                int j = 0;
                int width = Expensive.getInstance().getScaleMath().calc(res.getScaledWidth());
                boolean reverse = potionStatusDrag.getX() > width / 2;

                for (PotionEffect potionEffect : effects) {
                    Potion potion = potionEffect.getPotion();
                    String power = switch (potionEffect.getAmplifier()) {
                        case 0 -> "I";
                        case 1 -> "II";
                        case 2 -> "III";
                        case 3 -> "IV";
                        case 4 -> "V";
                        default -> "";
                    };
                    String s = I18n.format(potionEffect.getPotion().getName()) + " " + power;
                    String s2 = getDuration(potionEffect) + "";
                    float maxWidth = Math.max(Fonts.MONTSERRAT14.getStringWidth(s), Fonts.MONTSERRAT12.getStringWidth(s2)) + 32;
                    float maxHeight = Math.max(Fonts.MONTSERRAT14.getStringHeight(s), Fonts.MONTSERRAT12.getStringHeight(s2)) + 32;
                    potionStatusDrag.setWidth((int) (reverse ? maxWidth + 20 : maxWidth));
                    potionStatusDrag.setHeight(j);
                    GL11.glColor4f(1, 1, 1, 1);
                    this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                    if (reverse) {
                        //правая сторона поушинов
                        GlowUtility.drawGlowGradient(potionStatusDrag.getX() + 110 - maxWidth, potionStatusDrag.getY() + j + 6, maxWidth - 25, 20, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                        GlowUtility.drawGlowGradient (potionStatusDrag.getX() + 110 - maxWidth, potionStatusDrag.getY() + j + 6, maxWidth - 25, 20, 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
                        Fonts.MONTSERRAT14.drawString(s, potionStatusDrag.getX() + 4.0f - maxWidth + 110, potionStatusDrag.getY() + j + 11.5f, -1);
                        Fonts.MONTSERRAT12.drawString(s2, potionStatusDrag.getX() + 69, potionStatusDrag.getY() + j + 20, new Color(205, 205, 205, 205).getRGB());
                    } else {
                        //левая сторона поушинов
                        GlowUtility.drawGlowGradient(potionStatusDrag.getX() + 25, potionStatusDrag.getY() + j + 6, maxWidth - 25, 20, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
                        GlowUtility.drawGlowGradient (potionStatusDrag.getX() + 25, potionStatusDrag.getY() + j + 6, maxWidth - 25, 20, 10, Expensive.getInstance().getModuleManager().arraylist.getColor(80), Expensive.getInstance().getModuleManager().arraylist.getColor(160), Expensive.getInstance().getModuleManager().arraylist.getColor(280), Expensive.getInstance().getModuleManager().arraylist.getColor(280));
                          Fonts.MONTSERRAT14.drawString(s, potionStatusDrag.getX() + +27, potionStatusDrag.getY() + j + 11.5f, -1);
                        Fonts.MONTSERRAT12.drawString(s2, potionStatusDrag.getX() + 27, potionStatusDrag.getY() + j + 19f, new Color(205, 205, 205, 205).getRGB());
                    }
                    j += 20;
                }
            }
            if (showArmorHUD.get()) {
                ScaledResolution resolution = new ScaledResolution(mc);
                Expensive.getInstance().getScaleMath().pushScale();
                GL11.glColor4f(1, 1, 1, 1);
                final List<ItemStack> armor = new ArrayList<>();
                mc.entityRenderer.setupOverlayRendering(2);
                mc.player.getArmorInventoryList().forEach(armor::add);
                GL11.glPushMatrix();
                if (mc.player.getAir() < 300) {
                    GL11.glTranslatef(0, -8, 0);
                } else if (mc.player.capabilities.isCreativeMode) {
                    GL11.glTranslatef(0, 10, 0);

                }
                if (!armor.isEmpty())
                    for (int i = 0; i < 4; i++) {
                        ItemStack stack = armor.get(i);
                        if (!(stack.getItem() instanceof ItemAir)) {
                            String str = String.valueOf(stack.getMaxDamage() - stack.getItemDamage());
                            Fonts.SEMI_BOLD_16.drawString(str,
                                    resolution.getScaledWidth() / 2 - Fonts.SEMI_BOLD_16.getStringWidth(str) / 2 + 18 + i * 22,
                                    resolution.getScaledHeight() - 62, ColorUtility.rgba(200, 200, 200, 255));
                            RenderHelper.enableGUIStandardItemLighting();
                            drawItemStack(stack, resolution.getScaledWidth() / 2 + 10 + i * 22, Expensive.getInstance().getScaleMath().calc(resolution.getScaledHeight()) - 55);
                            RenderHelper.disableStandardItemLighting();
                        }
                    }
                GL11.glPopMatrix();
                mc.entityRenderer.setupOverlayRendering();
                Expensive.getInstance().getScaleMath().popScale();
            }
        }
    };

    private void drawItemStack(ItemStack stack, double x, double y) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
        GL11.glPopMatrix();
    }

    public static void drawItemTargetHUD(EntityPlayer player, float posX, float posY, float x2) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(player.getHeldItemMainhand(), player.getHeldItemOffhand()));
        for (int i = 1; i < 5; ++i) {
            ItemStack getEquipmentInSlot = player.getEquipmentInSlot(i);
            list.add(getEquipmentInSlot);
        }
        for (ItemStack itemStack : list) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(posX, posY, 1.0f);
            GlStateManager.scale(0.75f, 0.75f, 0.75f);
            GlStateManager.translate(-posX - 7, -posY + 10, 1.0f);
            RenderUtility.renderItem(itemStack, (int) x2 + 50, (int) (posY + 6));
            GlStateManager.popMatrix();
            x2 += 18.0f;
        }
    }

    public String calculateBPS() {
        return String.format("%.2f", Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ) * (double) mc.timer.timerSpeed * 20.0D);
    }

    public int calculatePing() {
        return mc.player.connection.getPlayerInfo(mc.player.getUniqueID()) != null ?
                mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : 0;
    }

    public static String getDuration(PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax()) {
            return "**:**";
        } else {
            return StringUtils.ticksToElapsedTime(potionEffect.getDuration());
        }
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
}
