package game.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
/**
 * Utility class for playing sound effects from the /sounds/ directory.
 */

public class SoundPlayer {
    /**
     * Plays the specified sound file from the /sounds/ resource folder.
     * @param fileName Name of the sound file
     */
    public static void playSound(String fileName) {
        try {
            URL soundURL = SoundPlayer.class.getResource("/sounds/" + fileName);
            if (soundURL == null) {
                System.err.println("Could not find sound file: " + fileName);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
