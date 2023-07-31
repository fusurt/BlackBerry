package wtf.expensive.modules.impl.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.movement.StrafeModule;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;


@SuppressWarnings("all")
@ModuleAnnotation(name = "TargetStrafe", desc = "Приследует противника", type = Type.MOVEMENT)
public class TargetStrafeModule extends Module {

    private SliderSetting distance = new SliderSetting("Distance", 2, 0.1f, 25, 0.5f);
    private SliderSetting speedValue = new SliderSetting("Speed", 0.23f, 0.1f, 2, 0.01f);
    private BooleanSetting jump = new BooleanSetting("Jump", true);

    private boolean switchDir = true;


    public EventListener<EventUpdate> e = e -> {
        EntityLivingBase entity = null;
        entity = AuraModule.instance.target;
        if (entity == null) return;

        double speed = speedValue.get();
        if (jump.get()) {
            if (mc.player.onGround) {
                mc.player.jump();
            }
        }
        double wrap = Math.atan2(mc.player.posZ - entity.posZ, mc.player.posX - entity.posX);
        wrap += switchDir ? speed / mc.player.getDistanceToEntity(entity)
                : -(speed / mc.player.getDistanceToEntity(entity));
        double x = entity.posX + distance.get() * Math.cos(wrap);
        double z = entity.posZ + distance.get() * Math.sin(wrap);
        if (switchCheck(x, z)) {
            switchDir = !switchDir;
            wrap += 2 * (switchDir ? speed / mc.player.getDistanceToEntity(entity)
                    : -(speed / mc.player.getDistanceToEntity(entity)));
            x = entity.posX + distance.get() * Math.cos(wrap);
            z = entity.posZ + distance.get() * Math.sin(wrap);
        }
        mc.player.motionX = speed * -Math.sin(Math.toRadians(wrapDS(x, z)));
        mc.player.motionZ = speed * Math.cos(Math.toRadians(wrapDS(x, z)));
    };

    public boolean switchCheck(double x, double z) {
        if (mc.player.isCollidedHorizontally || mc.gameSettings.keyBindLeft.isPressed()
                || mc.gameSettings.keyBindRight.isPressed()) {
            return true;
        }
        for (int l = (int) (mc.player.posY + 4); l >= 0; --l) {
            BlockPos playerPos = new BlockPos(x, l, z);
            blockFIRE:
            {
                blockLAVA:
                {
                    if (mc.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA))
                        break blockLAVA;
                    if (!mc.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE))
                        break blockFIRE;
                }
                return true;
            }
            if (mc.world.isAirBlock(playerPos))
                continue;
            return false;
        }
        return true;
    }

    private double wrapDS(double x, double z) {
        double diffX = x - mc.player.posX;
        double diffZ = z - mc.player.posZ;
        return Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
    }

}
