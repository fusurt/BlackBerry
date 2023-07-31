package wtf.expensive.modules.impl.render;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventJump;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.math.AnimationMath;
import wtf.expensive.utility.math.MathUtility;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;

@ModuleAnnotation(name = "JumpCircle", type = Type.RENDER)
public class JumpCircle extends Module {

    public List<Point> circles = new ArrayList<>();

    public EventListener<EventJump> jump = e -> {
        circles.add(new Point(mc.player.getPositionVector()));
    };

    public EventListener<EventDraw> draw = e -> {
        if (e.type == EventDraw.RenderType.RENDER) {

            circles.removeIf(circle -> (circle.radius > 1.5 - 0.065));

            if (circles.size() > 20) circles.remove(0);
            for (Point circe : circles) {
                circe.radius = AnimationMath.fast(circe.radius, 1.5f, 0.4f);
                circe.alpha = AnimationMath.fast(circe.alpha, 0, 0.000495f);

                int color1 = Expensive.getInstance().getModuleManager().arraylist.getColor(0);
                int color2 = Expensive.getInstance().getModuleManager().arraylist.getColor(90);
                int color3 = Expensive.getInstance().getModuleManager().arraylist.getColor(180);
                int color4 = Expensive.getInstance().getModuleManager().arraylist.getColor(240);

                double x = circe.pos.x - mc.getRenderManager().renderPosX,
                        y = circe.pos.y - mc.getRenderManager().renderPosY + 0.1,
                        z = circe.pos.z - mc.getRenderManager().renderPosZ;

                pushMatrix();
                translate(x - circe.radius / 2, y, z - circe.radius / 2);
                shadeModel(GL11.GL_SMOOTH);
                enableBlend();
                disableAlpha();
                depthMask(false);
                rotate(90, 1, 0, 0);

                mc.getTextureManager().bindTexture(new ResourceLocation("expensive/images/circle.png"));

                buffer.begin(GL11.GL_QUADS, POSITION_TEX_COLOR);
                {
                    buffer.pos(0, 0, 0).tex(0, 0).color(color1, circe.alpha).endVertex();
                    buffer.pos(0, circe.radius, 0).tex(0, 1).color(color2, circe.alpha).endVertex();
                    buffer.pos(circe.radius, circe.radius, 0).tex(1, 1).color(color3, circe.alpha).endVertex();
                    buffer.pos(circe.radius, 0, 0).tex(1, 0).color(color4, circe.alpha).endVertex();
                }
                tessellator.draw();

                enableAlpha();
                disableBlend();
                depthMask(true);
                popMatrix();

            }
        }
    };

    public static class Point {
        public Vec3d pos;
        public float radius;
        public long time;
        public float alpha = 255;

        public Point(Vec3d pos) {
            this.pos = pos;
            this.radius = 0;
            this.time = System.currentTimeMillis();
        }
    }

}