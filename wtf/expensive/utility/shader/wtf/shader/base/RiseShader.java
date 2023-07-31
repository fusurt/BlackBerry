package wtf.expensive.utility.shader.wtf.shader.base;


import wtf.expensive.utility.Utility;

import java.util.List;


public abstract class RiseShader implements Utility {
    private boolean active;

    public abstract void run(ShaderRenderType type, float partialTicks, List<Runnable> runnable);

    public abstract void update();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
