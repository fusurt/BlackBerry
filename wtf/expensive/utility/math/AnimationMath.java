package wtf.expensive.utility.math;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class AnimationMath {
    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }


    public static double easeInQuad(double t, double b, double c, double d) {
        t /= d;
        return c * t * t + b;
    }

    public static double easeOutQuad(double t, double b, double c, double d) {
        t /= d;
        return -c * t * (t - 2) + b;
    }

    public static double easeInOutQuad(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * t * t + b;
        t--;
        return -c / 2 * (t * (t - 2) - 1) + b;
    }

    public static double easeInCubic(double t, double b, double c, double d) {
        t /= d;
        return c * t * t * t + b;
    }

    public static double easeOutCubic(double t, double b, double c, double d) {
        t /= d;
        t--;
        return c * (t * t * t + 1) + b;
    }

    public static double easeInOutCubic(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * t * t * t + b;
        t -= 2;
        return c / 2 * (t * t * t + 2) + b;
    }

    public static double easeInQuart(double t, double b, double c, double d) {
        t /= d;
        return c * t * t * t * t + b;
    }

    public static double easeOutQuart(double t, double b, double c, double d) {
        t /= d;
        t--;
        return -c * (t * t * t * t - 1) + b;
    }

    public static double easeInOutQuart(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * t * t * t * t + b;
        t -= 2;
        return -c / 2 * (t * t * t * t - 2) + b;
    }

    public static double easeInQuint(double t, double b, double c, double d) {
        t /= d;
        return c * t * t * t * t * t + b;
    }

    public static double easeOutQuint(double t, double b, double c, double d) {
        t /= d;
        t--;
        return c * (t * t * t * t * t + 1) + b;
    }

    public static double easeInOutQuint(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * t * t * t * t * t + b;
        t -= 2;
        return c / 2 * (t * t * t * t * t + 2) + b;
    }

    public static double easeInSine(double t, double b, double c, double d) {
        return -c * Math.cos(t / d * (Math.PI / 2)) + c + b;
    }

    public static double easeOutSine(double t, double b, double c, double d) {
        return c * Math.sin(t / d * (Math.PI / 2)) + b;
    }

    public static double easeInOutSine(double t, double b, double c, double d) {
        return -c / 2 * (Math.cos(Math.PI * t / d) - 1) + b;
    }

    public static double easeInExpo(double t, double b, double c, double d) {
        return c * Math.pow(2, 10 * (t / d - 1)) + b;
    }

    public static double easeOutExpo(double t, double b, double c, double d) {
        return c * (-Math.pow(2, -10 * t / d) + 1) + b;
    }

    public static double easeInOutExpo(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
        t--;
        return c / 2 * (-Math.pow(2, -10 * t) + 2) + b;
    }

//    ease out bounce
    public static double easeOutBounce(double t, double b, double c, double d) {
        if ((t /= d) < (1 / 2.75)) {
            return c * (7.5625 * t * t) + b;
        } else if (t < (2 / 2.75)) {
            return c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b;
        } else if (t < (2.5 / 2.75)) {
            return c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375) + b;
        } else {
            return c * (7.5625 * (t -= (2.625 / 2.75)) * t + .984375) + b;
        }
    }

    public static double easeInBounce(double t, double b, double c, double d) {
        return c - easeOutBounce(d - t, 0, c, d) + b;
    }

    public static double easeInOutBounce(double t, double b, double c, double d) {
        if (t < d / 2) return easeInBounce(t * 2, 0, c, d) * .5 + b;
        else return easeOutBounce(t * 2 - d, 0, c, d) * .5 + c * .5 + b;
    }

    public static double easeInBack(double t, double b, double c, double d) {
        double s = 1.70158;
        return c * (t /= d) * t * ((s + 1) * t - s) + b;
    }

    public static double easeOutBack(double t, double b, double c, double d) {
        double s = 1.70158;
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }

    public static double easeInOutBack(double t, double b, double c, double d) {
        double s = 1.70158;
        if ((t /= d / 2) < 1) return c / 2 * (t * t * (((s *= (1.525)) + 1) * t - s)) + b;
        return c / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2) + b;
    }

    public static double easeInElastic(double t, double b, double c, double d) {
        double s = 1.70158;
        double p = 0;
        double a = c;
        if (t == 0) return b;
        if ((t /= d) == 1) return b + c;
        if (p == 0) p = d * .3;
        if (a < Math.abs(c)) {
            a = c;
            s = p / 4;
        } else {
            s = p / (2 * Math.PI) * Math.asin(c / a);
        }
        return -(a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
    }

    public static double easeOutElastic(double t, double b, double c, double d) {
        double s = 1.70158;
        double p = 0;
        double a = c;
        if (t == 0) return b;
        if ((t /= d) == 1) return b + c;
        if (p == 0) p = d * .3;
        if (a < Math.abs(c)) {
            a = c;
            s = p / 4;
        } else {
            s = p / (2 * Math.PI) * Math.asin(c / a);
        }
        return a * Math.pow(2, -10 * t) * Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b;
    }




    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtility.clamp((float) (AnimationMath.deltaTime() * multiple), 0, 1)) * end + MathUtility.clamp((float) (AnimationMath.deltaTime() * multiple), 0, 1) * start;
    }
    public static float lerp(float end, float start, float multiple) {
        return (float) (end + (start - end) * MathHelper.clamp(AnimationMath.deltaTime() * multiple, 0, 1));
    }
    public static double lerp(double end, double start, double multiple) {
        return (end + (start - end) * MathHelper.clamp(AnimationMath.deltaTime() * multiple, 0, 1));
    }


    public static float animation(float animation, float target, float speedTarget) {
        float dif = (target - animation) / Math.max((float) Minecraft.getDebugFPS(), 5) * 15;

        if (dif > 0) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        } else if (dif < 0) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }

    public static double Interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float calculateCompensation(float target, float current, float delta, double speed) {
        float diff = current - target;
        if (delta < 1.0f) {
            delta = 1.0f;
        }
        if (delta > 1000.0f) {
            delta = 16.0f;
        }
        double dif = Math.max(speed * (double) delta / 16.66666603088379, 0.5);
        if ((double) diff > speed) {
            if ((current = (float) ((double) current - dif)) < target) {
                current = target;
            }
        } else if ((double) diff < -speed) {
            if ((current = (float) ((double) current + dif)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static float Move(float from, float to, float minstep, float maxstep, float factor) {

        float f = (to - from) * MathHelper.clamp(factor, 0, 1);

        if (f < 0)
            f = MathHelper.clamp(f, -maxstep, -minstep);
        else
            f = MathHelper.clamp(f, minstep, maxstep);

        if (Math.abs(f) > Math.abs(to - from))
            return to;

        return from + f;
    }

    public static double animate(double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }

}
