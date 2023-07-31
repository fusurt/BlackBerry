package wtf.expensive.modules.impl.render;

import com.jhlabs.vecmath.Vector4f;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.expensive.Expensive;
import wtf.expensive.command.impl.CommandNameProtect;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleAnnotation(name = "EntityESP", type = Type.RENDER)
public class EntityESPModule extends Module {
    public final List<EntityPlayer> collectedEntities = new ArrayList<>();
    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;
    public MultiBoxSetting elements = new MultiBoxSetting("Elements Selection", new String[]{"Box", "Item", "Health", "Health Text", "Potions", "Armor Rect", "Name Tags"});

    public EntityESPModule() {
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
    }


    private final EventListener<EventDraw> onRender = e -> {
        if (e.type == EventDraw.RenderType.DISPLAY) {

            this.collectEntities();
            final float partialTicks = e.partialTicks;
            final RenderManager renderMng = mc.getRenderManager();
            final EntityRenderer entityRenderer = mc.entityRenderer;
            final List<EntityPlayer> collectedEntities = this.collectedEntities;

            for (EntityPlayer entity : collectedEntities) {

                double x = MathUtility.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = MathUtility.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = MathUtility.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);

                double width = entity.width / 1.5, height = entity.isChild() ? entity.height * 0.5f + 0.15f : entity.height + 0.1f - (entity.isSneaking() ? 0.2f : 0.0f);

                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);

                Vec3d[] vectors = {new Vec3d(aabb.minX, aabb.minY, aabb.minZ),
                        new Vec3d(aabb.minX, aabb.maxY, aabb.minZ),
                        new Vec3d(aabb.maxX, aabb.minY, aabb.minZ),
                        new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vec3d(aabb.minX, aabb.minY, aabb.maxZ),
                        new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ),
                        new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ),
                        new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};

                entityRenderer.setupCameraTransform(partialTicks, 0);

                Vector4f position = null;

                for (Vec3d vector : vectors) {

                    vector = this.project2D(2, vector.x - renderMng.viewerPosX, vector.y -
                            renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);

                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
                        }
                        position.x = (float) Math.min(vector.x, position.x);
                        position.y = (float) Math.min(vector.y, position.y);
                        position.z = (float) Math.max(vector.x, position.z);
                        position.w = (float) Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    entityRenderer.setupOverlayRendering(2);
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    if (elements.get(0)) {
                        Color color = Color.black;
                        RenderUtility.drawRectNotWH(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, color.getRGB());
                        RenderUtility.drawRectNotWH(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, color.getRGB());
                        RenderUtility.drawRectNotWH(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, color.getRGB());
                        RenderUtility.drawRectNotWH(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, color.getRGB());
                        color = Expensive.getInstance().friendManager.isFriend(entity.getName()) ? Color.GREEN : new Color(ColorUtility.getColorStyle(1));
                        RenderUtility.drawRectNotWH(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color.getRGB());
                        RenderUtility.drawRectNotWH(posX, endPosY - 0.5D, endPosX, endPosY, color.getRGB());
                        RenderUtility.drawRectNotWH(posX - 0.5D, posY, endPosX, posY + 0.5D, color.getRGB());
                        RenderUtility.drawRectNotWH(endPosX - 0.5D, posY, endPosX, endPosY, color.getRGB());
                    }
                    final double hpPercentage = entity.getHealth() / entity.getMaxHealth();
                    final double hpHeight2 = (endPosY - posY) * hpPercentage;
                    final double hpHeight3 = (endPosY - posY);
                    final double armorPercentage = entity.getTotalArmorValue();
                    double armorWidth = (endPosX - posX) * armorPercentage / 20.0;

                    if (elements.get(5)) {
                        RenderUtility.drawRectNotWH(posX - 0.5, endPosY + 1.0, posX - 0.5 + endPosX - posX, endPosY + 3.0, new Color(0, 0, 0, 120).getRGB());

                        if (armorPercentage > 0) {
                            RenderUtility.drawBorderedRect(posX - 0.5, endPosY + 1.0, armorWidth, 2.0, 0.5, Color.BLACK.getRGB(), Color.CYAN.getRGB());
                        }
                    }
                    if (entity.getHealth() > 0.0f && elements.get(2)) {

                        RenderUtility.drawRectNotWH((float) (posX - 3.5), (float) (endPosY + 0.5), (float) (posX - 1.5), (float) (endPosY - hpHeight3 - 0.5), new Color(17, 17, 17, 255).getRGB());
                        RenderUtility.verticalGradient((float) (posX - 3.0), (float) (posY), (float) (1.0), (float) (hpHeight3), new Color(116, 255, 122).getRGB(), new Color(241, 111, 111).getRGB());
                        RenderUtility.drawRectNotWH(posX - 3.5F, posY, posX - 1.5, (endPosY - hpHeight2), new Color(17, 17, 17, 255).getRGB());

                    }

                    if (!entity.getHeldItemMainhand().isEmpty() && elements.get(1)) {
                        Fonts.MCR8.drawCenteredStringWithShadow(ChatFormatting.stripFormatting(entity.getHeldItemMainhand().getDisplayName()), (float) (posX + (endPosX - posX) / 2.0D), (float) (endPosY + 0.5D) + (elements.get(5) ? 7 : 5), -1);
                    }
                    if (elements.get(3))
                        Fonts.MCR8.drawStringWithOutline((int) entity.getHealth() + "HP", (float) (posX - 4.5D) - Fonts.MCR8.getStringWidth((int) entity.getHealth() + "HP"), (float) (endPosY) - hpHeight2 + 4, getHealthColor(entity, new Color(241, 111, 111).getRGB(), new Color(116, 255, 122).getRGB()));
                    if (elements.get(4)) {
                        List<String> potions = new ArrayList<>();

                        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
                            String power = "";
                            ChatFormatting potionColor = ChatFormatting.GRAY;

                            if (potionEffect.getDuration() != 0) {
                                if (potionEffect.getAmplifier() == 0) {
                                    power = "I";
                                } else if (potionEffect.getAmplifier() == 1) {
                                    power = "II";
                                } else if (potionEffect.getAmplifier() == 2) {
                                    power = "III";
                                } else if (potionEffect.getAmplifier() == 3) {
                                    power = "IV";
                                } else if (potionEffect.getAmplifier() == 4) {
                                    power = "V";
                                }
                                potions.add(I18n.format(potionEffect.getPotion().getName()) + " " + power + TextFormatting.GRAY + " " + potionColor + getDuration(potionEffect));
                            }

                        }
                        float startY = (float) (posY + 3);

                        for (String s : potions) {
                            Fonts.MONTSERRAT12.drawStringWithShadow(s, posX + (endPosX - posX) + 5, startY, -1);
                            startY += 10;
                        }
                    }

                    if (elements.get(6)) {
                        float maxOffsetY = 0;
                        boolean isNameProtect = Expensive.getInstance().getModuleManager().get(NameProtect.class).state;
                        String protectName = CommandNameProtect.canChange ? CommandNameProtect.current : "Protected";
                        String name;
                        if (Expensive.getInstance().friendManager.isFriend(entity.getName())) {
                            name = isNameProtect ? ChatFormatting.GREEN + protectName.concat(" " +
                                    ColorUtility.getHealthStr(entity) + (int) entity.getHealth() + " HP")
                                    : ChatFormatting.GREEN + entity.getName().concat(" " +
                                    ColorUtility.getHealthStr(entity) + (int) entity.getHealth() + " HP");
                        } else if (isNameProtect && NameProtect.youtuber.get()) {
                            name = "Protected".concat(" " +
                                    ColorUtility.getHealthStr(entity) + (int) entity.getHealth() + " HP");
                        } else if (isNameProtect && entity.getDisplayName().getUnformattedText().contains(mc.player.getName())) {
                            name = protectName.concat(" " +
                                    ColorUtility.getHealthStr(entity) + (int) entity.getHealth() + " HP");
                        } else {
                            name = entity.getDisplayName().getFormattedText().concat(" " +
                                    ColorUtility.getHealthStr(entity) + (int) entity.getHealth() + " HP");
                        }

                        Fonts.SEMI_BOLD_14.drawStringWithShadow(name, (float) (posX + (endPosX - posX) / 2f)
                                        - (name.isEmpty() ? 0 : Fonts.SEMI_BOLD_14.getStringWidth(name)) / 2, (float) posY - 10,
                                -1);
                        maxOffsetY += 20;
                        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(entity.getHeldItemMainhand(), entity.getHeldItemOffhand()));
                        entity.getArmorInventoryList().forEach(stacks::add);
                        stacks.removeIf(w -> w.getItem() instanceof ItemAir);
                        int totalSize = stacks.size() * 10;
                        maxOffsetY += 19;
                        int iterable = 0;
                        for (ItemStack stack : stacks) {
                            if (stack != null) {
                                RenderHelper.enableGUIStandardItemLighting();
                                drawItemStack(stack, (float) (posX + (endPosX - posX) / 2f) + iterable * 20 - totalSize + 2,
                                        posY - maxOffsetY + 10, null, false);
                                RenderHelper.disableStandardItemLighting();
                                iterable++;
                                final ArrayList<String> lines = new ArrayList<>();

                                getEnchantment(lines, stack);

                                int i = 0;
                                for (String s : lines) {

                                    Fonts.MONTSERRAT12.drawCenteredStringWithShadow(s,
                                            (float) (posX + (endPosX - posX) / 2f) + iterable * 20 - totalSize - 10,
                                            (float) posY - maxOffsetY + 5 - (i * 7),
                                            0xFFFFFFFF);
                                    i++;
                                }
                            }
                        }

                    }
                }
                mc.entityRenderer.setupOverlayRendering();
            }
        }
    };


    public static void getEnchantment(ArrayList<String> list, ItemStack stack) {
        Item item = stack.getItem();
        if (list == null) return;
        int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
        int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
        int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
        int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
        int vanishing_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_190940_C, stack);
        int binding_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_190941_k, stack);
        int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_190940_C, stack);
        int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
        int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
        int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        int fireprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
        int blastprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

        if (item instanceof ItemAxe) {
            if (sharpness > 0) {
                list.add("Shr" + sharpness);
            }
            if (efficiency > 0) {
                list.add("Eff" + efficiency);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemArmor) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (fireprot > 0) {
                list.add("Firp" + fireprot);
            }
            if (blastprot > 0) {
                list.add("Bla" + blastprot);
            }
            if (binding_curse > 0) {
                list.add("�cBindi ");
            }
            if (depth > 0) {
                list.add("Dep" + depth);
            }
            if (feather > 0) {
                list.add("Fea" + feather);
            }
            if (protection > 0) {
                list.add("Pro" + protection);
            }
            if (thorns > 0) {
                list.add("Thr" + thorns);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemBow) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (binding_curse > 0) {
                list.add("Binding ");
            }
            if (infinity > 0) {
                list.add("Inf" + infinity);
            }
            if (power > 0) {
                list.add("Pow" + power);
            }
            if (punch > 0) {
                list.add("Pun" + punch);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (flame > 0) {
                list.add("Fla" + flame);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemSword) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (looting > 0) {
                list.add("Loot" + looting);
            }
            if (binding_curse > 0) {
                list.add("Bindi ");
            }
            if (sweeping > 0) {
                list.add("Swe" + sweeping);
            }
            if (sharpness > 0) {
                list.add("Shr" + sharpness);
            }
            if (knockback > 0) {
                list.add("Kno" + knockback);
            }
            if (fireAspect > 0) {
                list.add("Fir" + fireAspect);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
            if (mending > 0) {
                list.add("Men ");
            }
        }
        if (item instanceof ItemTool) {
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (binding_curse > 0) {
                list.add("Binding ");
            }
            if (efficiency > 0) {
                list.add("Eff" + efficiency);
            }
            if (silktouch > 0) {
                list.add("Sil" + silktouch);
            }
            if (fortune > 0) {
                list.add("For" + fortune);
            }
        }
    }

    public static void drawItemStack(ItemStack stack, double x, double y, String altText, boolean withoutOverlay) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (!withoutOverlay) mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, altText);
        GL11.glPopMatrix();
    }

    public static String getDuration(PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax()) {
            return "**:**";
        } else {
            return StringUtils.ticksToElapsedTime(potionEffect.getDuration());
        }
    }

    private boolean isValid(final Entity entity) {
        if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (!RenderUtility.isInViewFrustum(entity)) return false;
        return entity instanceof EntityPlayer;
    }

    public static int getHealthColor(final EntityLivingBase entity, final int c1, final int c2) {
        final float health = entity.getHealth();
        final float maxHealth = entity.getMaxHealth();
        final float hpPercentage = MathUtility.clamp(health / maxHealth, 0, 1);
        final int red = (int) ((c2 >> 16 & 0xFF) * hpPercentage + (c1 >> 16 & 0xFF) * (1.0f - hpPercentage));
        final int green = (int) ((c2 >> 8 & 0xFF) * hpPercentage + (c1 >> 8 & 0xFF) * (1.0f - hpPercentage));
        final int blue = (int) ((c2 & 0xFF) * hpPercentage + (c1 & 0xFF) * (1.0f - hpPercentage));
        return new Color(red, green, blue).getRGB();
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        final List<EntityPlayer> playerEntities = mc.world.playerEntities;
        for (final EntityPlayer entity : playerEntities) {
            if (!this.isValid(entity)) {
                continue;
            }
            this.collectedEntities.add(entity);

        }
    }

    private Vec3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector)) {
            return new Vec3d(this.vector.get(0) / scaleFactor, (Display.getHeight() - this.vector.get(1)) / scaleFactor, this.vector.get(2));
        }
        return null;
    }
}
