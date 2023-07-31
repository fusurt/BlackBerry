package wtf.expensive.utility.shader.wtf.shader;


import wtf.expensive.utility.shader.wtf.shader.base.RiseShader;
import wtf.expensive.utility.shader.wtf.shader.impl.*;

public interface RiseShaders {
    RiseShader GAUSSIAN_BLUR_SHADER = new GaussianBlurShader();
    RQShader RQ = new RQShader();
}
