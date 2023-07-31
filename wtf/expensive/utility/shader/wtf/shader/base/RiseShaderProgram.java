package wtf.expensive.utility.shader.wtf.shader.base;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import wtf.expensive.utility.Utility;
import wtf.expensive.utility.shader.wtf.shader.ShaderUtil;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class RiseShaderProgram implements Utility {

    private final int programId;

    public RiseShaderProgram(final String fragmentPath, final String vertexPath) {
        this.programId = ShaderUtil.createShader(fragmentPath, vertexPath);
    }

    public static void drawQuad(final double x, final double y, final double width, final double height) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(0.0F, 0.0F).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(1.0F, 0.0F).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(1.0F, 1.0F).endVertex();
        buffer.pos(x, y, 0.0D).tex(0.0F, 1.0F).endVertex();
        Tessellator.getInstance().draw();
    }


    public int getUniform(String name) {
        return glGetUniformLocation(getProgramId(), name);
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(getProgramId(), name);
        switch (args.length) {
            case 1:
                glUniform1f(loc, args[0]);
                break;
            case 2:
                glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(getProgramId(), name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public static void drawQuad() {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        drawQuad(0.0, 0.0, scaledResolution.getScaledWidth_double(), scaledResolution.getScaledHeight_double());
    }

    public void start() {
        GL20.glUseProgram(programId);
    }

    public static void stop() {
        GL20.glUseProgram(0);
    }

    public int getProgramId() {
        return programId;
    }
}
