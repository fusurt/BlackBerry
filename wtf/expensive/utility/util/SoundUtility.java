package wtf.expensive.utility.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundUtility {
    public static float DEFAULT_PITCH = 1.7f;

    public static void playSound(String sound, float volume) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                InputStream audioSrc = SoundUtility.class.getResourceAsStream("/assets/minecraft/expensive/sounds/" + sound);
                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);

                clip.open(inputStream);
                clip.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void playSound(String sound, EntityPlayer player) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                InputStream audioSrc = SoundUtility.class.getResourceAsStream("/assets/minecraft/client/sounds/" + sound);
                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                FloatControl v = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                v.setValue(1 - Minecraft.getMinecraft().player.getDistanceToEntity(player) * 5);

                clip.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void playSound() {
        Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_NOTE_PLING, 1.0f, 1.7f);
    }

    public static void playSound(float pitch, float volume) {
        if (Minecraft.getMinecraft().player == null)
            return;
        Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_NOTE_PLING, volume, pitch);

    }

    public static void playSound(float pitch) {
        playSound(pitch, 3);
    }
}
