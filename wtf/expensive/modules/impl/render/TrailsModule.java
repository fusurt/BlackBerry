package wtf.expensive.modules.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.color.ColorUtility;


import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_GREATER;

@ModuleAnnotation(name = "Trails", desc = "Показывает линию позади вас", type = Type.RENDER)
public class TrailsModule extends Module {

    public ArrayList<Point> p = new ArrayList<>();

    private final SliderSetting removeTicks = new SliderSetting("Delete through", 100, 1, 500, 1);



    public EventListener<EventDraw> eventListener = e -> {
        setDisplayName(removeTicks.get() + "");
        if ((mc.gameSettings.thirdPersonView == 1 || mc.gameSettings.thirdPersonView == 2) && e.type == EventDraw.RenderType.RENDER) {


            double ix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.partialTicks);
            double iy = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.partialTicks);
            double iz = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.partialTicks);
            // interpolated player position
            float x = (float) (float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.partialTicks);
            float y = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.partialTicks);
            float z = (float) (float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.partialTicks);
            //update
            if (!(mc.player.motionX == 0) || !(mc.player.motionZ == 0))
                p.add(new Point(new Vec3d(x, y, z)));
            p.removeIf(point -> point.time >= removeTicks.get());

            GlStateManager.pushMatrix();
            mc.entityRenderer.setupCameraTransform(e.partialTicks, 2);
            GlStateManager.translate(ix, iy, iz);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);

            // trail
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            for (Point point : p) {
                Color color = new Color(Expensive.getInstance().styleManager.getCurrentStyle().getColor(p.indexOf(point)));

                if (p.indexOf(point) >= p.size() - 1) continue;
                float alpha = 100 * (p.indexOf(point) / (float) p.size());
                Point temp = p.get(p.indexOf(point) + 1);
                int color2 = ColorUtility.setAlpha(new Color(color.getRGB()).getRGB(), (int) alpha);
                ColorUtility.setColor(color2);
                GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
                GL11.glVertex3d(temp.pos.x, temp.pos.y + mc.player.height - 0.1, temp.pos.z);
                point.time++;
            }
            GL11.glEnd();

            // line
            GL11.glLineWidth(2);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Point point : p) {
                Color color = new Color(Expensive.getInstance().styleManager.getCurrentStyle().getColor(p.indexOf(point)));

                if (p.indexOf(point) >= p.size() - 1) continue;
                float alpha = new Color(color.getRGB()).getAlpha() * (p.indexOf(point) / (float) p.size());
                Point temp = p.get(p.indexOf(point) + 1);
                int color2 = ColorUtility.setAlpha(ColorUtility.fade(5, p.indexOf(point) * 10, new Color(color.getRGB()), 1).brighter().getRGB(), (int) alpha);
                ColorUtility.setColor(color2);
                GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);

                point.time++;
            }
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Point point : p) {
                Color color = new Color(Expensive.getInstance().styleManager.getCurrentStyle().getColor(p.indexOf(point)));
                if (p.indexOf(point) >= p.size() - 1) continue;
                float alpha = new Color(color.getRGB()).getAlpha() * (p.indexOf(point) / (float) p.size());
                Point temp = p.get(p.indexOf(point) + 1);
                int color2 = ColorUtility.setAlpha(ColorUtility.fade(5, p.indexOf(point) * 10, new Color(color.getRGB()), 1).brighter().getRGB(), (int) alpha);
                ColorUtility.setColor(color2);
                GL11.glVertex3d(temp.pos.x, temp.pos.y + mc.player.height - 0.1, temp.pos.z);
                point.time++;
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            mc.entityRenderer.setupCameraTransform(e.partialTicks, 0);
            GL11.glPopMatrix();
        }

    };
    public static class Point {
        public Vec3d pos;
        public long time;

        public Point(Vec3d pos) {
            this.pos = pos;
        }
    }
}
