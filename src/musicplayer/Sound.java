package musicplayer;

import java.io.*;
import java.math.BigDecimal;

import javax.sound.sampled.*;

import devLogger.LoggerWrapper;

/**
 * 
 * This class implements a simple wav music player and using the MusicManger
 * class for automatically playing next songs.
 * 
 * 
 * @author Florian Berndl
 */
public class Sound implements LineListener {

	private Clip					clip;
	private MusicManager	menu;
	private boolean				islooped;

	/**
	 * 
	 * 
	 * @param musicManager Is used in update function for playing next song in play list.
	 */
	Sound(MusicManager musicManager) {
		LoggerWrapper.getInstance().getMyLogger().fine("Sound constructor");
		this.menu = musicManager;
	}

	/*
	 * Stops the current clip and go to beginning of the clip
	 */
	public void stop() {
		LoggerWrapper.getInstance().getMyLogger().fine("Sound stop");
		pause();
		clip.setMicrosecondPosition(0);
	}

	/*
	 * Next An LineEvent.STOP is generated and the clip position is set to the end
	 * of the clip. The next song will be started immediately.Clip must be already
	 * started.
	 */
	public void next() {
		LoggerWrapper.getInstance().getMyLogger().fine("Sound next");
		clip.setMicrosecondPosition(clip.getMicrosecondLength());
		clip.stop();
	}

	/*
	 * Pause an played clip
	 */
	public void pause() {
		if (clip.isOpen() && clip.isRunning()) {
			LoggerWrapper.getInstance().getMyLogger().fine("Sound pause");
			clip.stop();
		} else {
			LoggerWrapper.getInstance().getMyLogger().info("Sound pause warning not playing!");
		}
	}

	/*
	 * Resumes from paused clip.
	 */
	public void resume() {
		if (clip.isOpen()) {
			LoggerWrapper.getInstance().getMyLogger().fine("Sound resume");
			clip.start();
			// When clip is already ended, create event..
			if (clip.isRunning() == false)
				update(new LineEvent(clip, LineEvent.Type.STOP, clip.getMicrosecondLength()));
		} else {
			LoggerWrapper.getInstance().getMyLogger().severe("Error can not resume sound!!");
			//System.err.println("Error can not resume sound!!");
			//next();
		}

	}

	
	/**
	 * 
	 * Plays wav sound clips
	 * 
	 * @param str Pathstring to Song
	 * @param volume Volume value beetween 0 and 1
	 * @throws UnsupportedAudioFileException
	 */
	public void playSoundClip(String str, BigDecimal volume) throws UnsupportedAudioFileException {
		System.out
				.print("\r                                                                                ");
		System.out.print("\r Playing: " + str);

		try {
			if (clip != null) {
				clip.close();
			}
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(str));
			clip = AudioSystem.getClip();
			clip.open(audio);
			clip.addLineListener(this);
			setVolume(volume);
			clip.start();
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (LineUnavailableException lua) {
			System.out.println(lua);
		}

	}

	/**
	 * set Volume of clips
	 * 
	 * @param gain BigDecimal value beetween 0 and 1.
	 */
	public void setVolume(BigDecimal gain) {
		if (clip.isOpen()) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			// gain number between 0 and 1 (loudest)
			float dB = (float) (Math.log(gain.doubleValue()) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
	}

	/**
	 * 
	 * @return boolean isPause
	 */
	public boolean isPause() {
		return !clip.isRunning();
	}

	/*
	 * When clip is stopped and the and has reached, the next song function of the
	 * MusicManager is caled and the clip is closed. Informs the listener that a
	 * line's state has changed. The listener can then invoke LineEvent methods to
	 * obtain information about the event.
	 * 
	 * @param event -a line event that describes the change
	 */
	@Override
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.STOP) {

			if (clip.getMicrosecondLength() == clip.getMicrosecondPosition()) {
				clip.drain();
				clip.close();
				LoggerWrapper.getInstance().getMyLogger().info("LineEvent STOP");
				if (!loopSong()) {
					menu.nextSong();
				} else {
					menu.playSong();
				}

			}

		}
	}

	/*
	 * Activates and deactivate clip looping
	 * 
	 * @return islooped The actual looped boolean state
	 */
	public boolean loopSong() {
		if (clip.isOpen()) {

			if (islooped == true) {
				islooped = false;
			} else {
				islooped = true;
			}
		}
		return islooped;

	}
}