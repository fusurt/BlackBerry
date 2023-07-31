package wtf.expensive.modules.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.TimerUtility;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.movement.MoveUtility;
import wtf.expensive.utility.util.ChatUtility;
import wtf.expensive.utility.util.InventoryUtility;

@ModuleAnnotation(name = "ElytraFly", type = Type.MOVEMENT)
public class ElytraFly extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Matrix Fireworks");
    public SliderSetting speed = new SliderSetting("Speed", 0.7f, 0.1f, 1.0F, 0.01f, () -> mode.is("Matrix"));

    public SliderSetting motionY = new SliderSetting("Motion Y", 0.5f, 0.01f, 1, 0.01f, () -> mode.is("Matrix"));

    public BooleanSetting autoJump = new BooleanSetting("Auto Jump", true, () -> mode.is("Matrix"));

    public TimerUtility time = new TimerUtility(), time2 = new TimerUtility();
    public static long lastStartFalling;
    public int lastItem;

    private final EventListener<EventUpdate> onUpdate = event -> {
        setDisplayName(mode.get());

        if (mode.is("Matrix Fireworks")) {
            matrixFireworks();
        } else if (mode.is("Matrix")) {
            matrix();
        }

    };

    public void matrixFireworks() {
        if (InventoryUtility.getFireWorks() == -1) {
            ChatUtility.addChatMessage("Вам нужны феерверки для использования данного мода!");
            this.toggle();
            return;
        }
        int elytra = getHotbarSlotOfItem();
        if (elytra == -1) {
            ChatUtility.addChatMessage("Вам нужен элитра для использования данного мода!");
            toggle();
            return;
        }
        if (MoveUtility.reason(false)) {
            return;
        }
        if (mc.player.onGround) {
            mc.player.jump();
            time.reset();
        } else if (time.hasTimeElapsed(350)) {


            if (!mc.player.onGround) {
                mc.player.motionY = 0.0F;
                if (!mc.player.wasFallFlying && mc.player.ticksExisted % 4 == 0) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    mc.player.jump();

                } else if (mc.player.wasFallFlying) {
                    if (mc.player.ticksExisted % 25 == 0) {

                        if (InventoryUtility.getFireWorks() >= 0) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtility.getFireWorks()));
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                        }
                    }

                    MoveUtility.setMotion(MoveUtility.getMotion());
                    if (!mc.player.isSneaking() && mc.gameSettings.keyBindJump.pressed) {
                        mc.player.motionY = 0.8f;
                    }
                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.player.motionY = -0.8f;
                    }

                }
            }
        }

    }

    public void matrix() {
        int elytra = getHotbarSlotOfItem();

        if (MoveUtility.reason(false)) {
            return;
        }
        if (elytra == -1) {
            ChatUtility.addChatMessage("Вам нужен элитра для использования данного мода!");
            toggle();
            return;
        }
        if (mc.player.onGround) {
            if (autoJump.get()) {
                mc.player.jump();
            }
            time.reset();
        } else if (time.hasTimeElapsed(350)) {

            if (mc.player.ticksExisted % 2 == 0) {
                disabler(elytra);

            }

            mc.player.motionY = mc.player.ticksExisted % 2 != 0 ? -0.25 : 0.25;


            if (!mc.player.isSneaking() && mc.gameSettings.keyBindJump.pressed) {
                mc.player.motionY = motionY.get();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -motionY.get();
            }
            MoveUtility.setMotion(speed.get());

        }
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        int elytra = getHotbarSlotOfItem();
        if (mode.is("Matrix Fireworks")) {
            if (elytra != -1) {

                lastItem = elytra;

                mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, lastItem, 1, ClickType.PICKUP, mc.player);

            }
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        int elytra = getHotbarSlotOfItem();
        if (mode.is("Matrix Fireworks")) {
            if (elytra != -1) {

                if (elytra == -2) {
                    mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, this.lastItem, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                }
            }
        }
        super.onDisable();
    }

    public static void disabler(int elytra) {
        if (elytra != -2) {
            mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
        }
        mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));

        if (elytra != -2) {
            mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
        }
        lastStartFalling = System.currentTimeMillis();
    }

    public static int getHotbarSlotOfItem() {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem() == Items.ELYTRA) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == Items.ELYTRA) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }
}
