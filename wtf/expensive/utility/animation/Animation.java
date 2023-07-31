package wtf.expensive.utility.animation;

import wtf.expensive.utility.math.AnimationMath;

public class Animation {
    public long lastTime;
    public long speed;
    public double current;


    public Animation(long speed) {
        this.speed = speed;
    }


    public void update(float value) {
        speed = 10000;
        long time = (System.currentTimeMillis() - lastTime);
        time /= 1000;

//      easings animations
            current = AnimationMath.fast((float) current, value, time);
            lastTime = System.currentTimeMillis();

    }

    // easing functions


}
