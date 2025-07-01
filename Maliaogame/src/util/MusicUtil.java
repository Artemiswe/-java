package util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicUtil {
    private static Clip clip;
    static {
        File bgMusicFile = new File("music/bg1.wav");
        try {
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(bgMusicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playBackground(){
    	//“Ù¿÷≤•∑≈
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
