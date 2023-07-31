package wtf.expensive.modules.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.color.ColorUtility;

import java.util.HashMap;

@ModuleAnnotation(name = "PearlPrediction", type = Type.RENDER)
public class PearlPredictionModule extends Module {

    private final EventListener<EventDraw> onDraw = event -> {

        if (event.type != EventDraw.RenderType.RENDER) {
            return;
        }
        double ix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.partialTicks);
        double iy = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.partialTicks);
        double iz = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate(ix, iy, iz);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(3);
        GL11.glColor4f(0, 0, 0, 1);
        GL11.glBegin(GL11.GL_LINES);

        {
            for (Entity en : mc.world.loadedEntityList) {
                if (en instanceof EntityEnderPearl) {
                    penisbobra(en, ((EntityEnderPearl) en).getGravityVelocity(), 0.8F, false);
                }
            }
        }

        GL11.glEnd();
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINES);

        {
            for (Entity en : mc.world.loadedEntityList) {
                if (en instanceof EntityEnderPearl) {
                    penisbobra(en, ((EntityEnderPearl) en).getGravityVelocity(), 0.8F, true);
                }
            }
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();

    };
    public HashMap<Entity, Vec3d> lastPoss = new HashMap<>();
    public HashMap<Entity, Integer> i1 = new HashMap<>();

    private void penisbobra(Entity e, double g, double water, boolean r) {
        double motionX = e.motionX;
        double motionY = e.motionY;
        double motionZ = e.motionZ;
        double x = e.posX;
        double y = e.posY;
        double z = e.posZ;
        Vec3d lastPos = new Vec3d(x, y, z);

        for (int i = 0; i < 300; i++) {
            if (r)
                ColorUtility.setColor(ColorUtility.getColorStyle(i * 4));
            lastPos = new Vec3d(x, y, z);
            x += motionX;
            y += motionY;
            z += motionZ;
            if (mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() == Blocks.WATER) {
                motionX *= water;
                motionY *= water;
                motionZ *= water;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY -= g;
            Vec3d pos = new Vec3d(x, y, z);
            if (mc.world.rayTraceBlocks(lastPos, pos) != null) {

                if (mc.world.rayTraceBlocks(lastPos, pos).typeOfHit == RayTraceResult.Type.ENTITY) {
                    break;
                }
                break;
            }

            if (y <= 0) break;
            if (e.motionZ == 0 && e.motionX == 0 && e.motionY == 0) continue;
            lastPoss.put(e, new Vec3d(lastPos.x, lastPos.y, lastPos.z));
            GL11.glVertex3d(lastPos.x, lastPos.y, lastPos.z);
            GL11.glVertex3d(x, y, z);
            i1.put(e, i);


        }

    }
}
