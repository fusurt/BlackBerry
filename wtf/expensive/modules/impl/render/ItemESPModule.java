package wtf.expensive.modules.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.render.RenderUtility;

import javax.vecmath.Vector4d;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@ModuleAnnotation(name = "ItemESP", type = Type.RENDER)
public class ItemESPModule extends Module {

    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;

    public ItemESPModule() {
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
    }
    private final EventListener<EventDraw> onDraw = event -> {
        if (event.type == EventDraw.RenderType.DISPLAY) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    if (RenderUtility.isInViewFrustum(entity)) {
                        double x = entity.lastTickPosX
                                + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
                        double y = entity.lastTickPosY
                                + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
                        double z = entity.lastTickPosZ
                                + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
                        double width = entity.width / 1.5;
                        double height = entity.height + 0.2F;
                        final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                        final Vec3d[] vectors = {new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};


                        mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);

                        Vector4d position = null;
                        for (Vec3d vector : vectors) {
                            vector = project2D(2,
                                    vector.x - mc.getRenderManager().renderPosX,
                                    vector.y - mc.getRenderManager().renderPosY,
                                    vector.z - mc.getRenderManager().renderPosZ);
                            if (vector != null && vector.z > 0 && vector.z < 1) {

                                if (position == null) {
                                    position = new Vector4d(vector.x, vector.y, vector.z, 0);
                                }

                                position.x = Math.min(vector.x, position.x);
                                position.y = Math.min(vector.y, position.y);
                                position.z = Math.max(vector.x, position.z);
                                position.w = Math.max(vector.y, position.w);
                            }
                        }

                        if (position != null) {
                            mc.entityRenderer.setupOverlayRendering(2);
                            double posX = position.x;
                            double posY = position.y;
                            double endPosX = position.z;
                            int build = -1;


                            String tag = ((EntityItem) entity).getEntityItem().getDisplayName()
                                    + (((EntityItem) entity).getEntityItem().stackSize < 1 ? ""
                                    : " x" + ((EntityItem) entity).getEntityItem().stackSize);
                            Fonts.SEMI_BOLD_12.drawStringWithShadow(tag,
                                    (float) ((posX + ((endPosX - posX) / 2)
                                            - Fonts.SEMI_BOLD_12.getStringWidth(tag) / 2)),
                                    (float) (posY - 10), build);
                        }

                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GlStateManager.enableBlend();
                        mc.entityRenderer.setupOverlayRendering();
                    }
                }
            }
        }
    };
    private Vec3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector)) {
            return new Vec3d(this.vector.get(0) / 2, (Display.getHeight() - this.vector.get(1)) / 2, this.vector.get(2));
        }
        return null;
    }
}
