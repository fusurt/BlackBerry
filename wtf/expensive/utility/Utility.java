package wtf.expensive.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import wtf.expensive.utility.shader.BloomUtil;
import wtf.expensive.utility.shader.GaussianBlur;
import wtf.expensive.utility.shader.wtf.shader.RiseShaders;
import wtf.expensive.utility.shader.wtf.shader.base.ShaderRenderType;

import java.util.ArrayList;
import java.util.List;

public interface Utility {

    Minecraft mc = Minecraft.getMinecraft();
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    List<Runnable> NORMAL_BLUR_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_SHADOW_BLACK = new ArrayList<>();

    static void render2DRunnables() {

        GaussianBlur.renderBlur(8, NORMAL_BLUR_RUNNABLES);
        BloomUtil.renderBlur(NORMAL_SHADOW_BLACK);
        clearRunnables();

    }

    static void clearRunnables() {
        NORMAL_BLUR_RUNNABLES.clear();
        NORMAL_SHADOW_BLACK.clear();
    }


}
