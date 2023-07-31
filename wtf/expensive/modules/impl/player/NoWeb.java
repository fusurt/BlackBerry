package wtf.expensive.modules.impl.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventMotion;
import wtf.expensive.event.impl.player.EventPreMove;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.math.MathUtility;
import wtf.expensive.utility.movement.MoveUtility;

@ModuleAnnotation(name = "NoWeb", type = Type.PLAYER)
public class NoWeb extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Matrix", "WebLeave");
    SliderSetting motionY = new SliderSetting("Motion Y", 8, 1, 10, 1,
            () -> mode.is("WebLeave"));
   public static float move;
    public EventListener<EventPreMove> onPreMove = e -> {
        if (mode.is("Matrix")) {
            if (mc.player.isInWeb) {
                move = 10;
                MoveUtility.setEventSpeed(e, 0.483f);
            } else {
                move--;
            }
        }
    };
    public EventListener<EventMotion> onMove = e -> {
        setDisplayName(mode.get());
        if (mode.is("Matrix")) {

            if (mc.player.isInWeb) {
                move = 10;
                mc.player.motionY = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY += 2;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY -= 2;
                }

            } else {
                move--;
            }
        } else if (mode.is("Vanilla")) {
            if (mc.player.isInWeb) {
                mc.player.isInWeb = false;
            }
        } else if (mode.is("WebLeave")) {
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.6, mc.player.posZ);
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (mc.player.isInWeb) {
                mc.player.motionY += 0.5;
            } else if (Block.getIdFromBlock(block) == 30) {
                mc.player.motionX = mc.player.motionZ = 0;
                mc.player.motionY += motionY.get();
                mc.gameSettings.keyBindJump.pressed = false;
            }
        }
    };

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}
