/**
 * 
 */
package musicplayer;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import javax.sound.sampled.UnsupportedAudioFileException;

import devLogger.LoggerWrapper;

/**
 * This class is a simple java class for playing sound files of an playlist.
 * It uses the Sound class for playing,pause,resume,loop songs and to set Volume.
 * 
 * @author Florian Berndl
 */
public class MusicManager {

	private Sound					sound;
	private List<String>	playlist;
	private Integer				current_sound	= -1;
	private BigDecimal		Volume				= new BigDecimal(0.2D);
	private BigDecimal		Volume_step;

	/**
	 * 
	 * @param playlist A list of *.wav files.
	 * @param Volume_step BigDecimal value of volume steps (e.g. 0.2). Between 0 and 1
	 */
	MusicManager(List<String> playlist, BigDecimal Volume_step) {
		sound = new Sound(this);
		this.playlist = playlist;
		this.Volume_step = Volume_step;
	}

	/*
	 * Decrease the volume of songs by </code>Volume_step <code>.
	 */
	void VolumeDown() {
		if (Volume.compareTo(Volume_step) >= 0) {
			LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "Volume down " + Volume);
			Volume = Volume.subtract(Volume_step);
			sound.setVolume(Volume);
		} else {
			LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "Volume reached min");
		}

	}

	/*
	 * Increase the volume of songs by </code>Volume_step <code>.
	 */
	void VolumeUp() {

		if (Volume.compareTo(new BigDecimal(1 - Volume_step.doubleValue())) <= 0) {
			LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "Volume up" + Volume);
			Volume = Volume.add(Volume_step);
			sound.setVolume(Volume);
		} else {
			LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "Volume is max");
		}

	}

	/*
	 * Resume or pause songs.
	 */
	void pauseResume() {
		LoggerWrapper.getInstance().getMyLogger()
				.log(Level.FINE, "MENU PLAY/PAUSE" + playlist.get(current_sound));
		if (sound.isPause() == true) {
			sound.resume();
		} else {
			sound.pause();
		}

	}

	/*
	 * Playing previous Song
	 */
	void previousSong() {
		// wrap around or increase
		if (current_sound == 0) {
			current_sound = playlist.size() - 1;
		} else {
			current_sound--;
		}
		LoggerWrapper.getInstance().getMyLogger()
				.log(Level.FINE, "MENU PREV" + playlist.get(current_sound));
		try {
			sound.playSoundClip(playlist.get(current_sound), Volume);
		} catch (UnsupportedAudioFileException e) {
			previousSong();
			LoggerWrapper.getInstance().getMyLogger().log(Level.WARNING, "", e);
		}
	}

	/*
	 * Playing next song in playlist
	 */
	public void nextSong() {
		// wrap around or increase
		if (current_sound == playlist.size() - 1) {
			current_sound = 0;
		} else {
			current_sound++;
		}
		playSong();

	}

	/*
	 * Plays current_sound in playlist and catch UnsupportedAudioFileException in
	 * the case of unsupported audio files.
	 */
	public void playSong() {
		LoggerWrapper.getInstance().getMyLogger()
				.log(Level.FINE, "MENU NEXT" + playlist.get(current_sound));
		try {
			sound.playSoundClip(playlist.get(current_sound), Volume);
		} catch (UnsupportedAudioFileException e) {
			nextSong();
			LoggerWrapper.getInstance().getMyLogger().log(Level.WARNING, "", e);
		}
	}

	public boolean loopSong() {
		return sound.loopSong();
	}

	public double getVolume() {
		return Volume.doubleValue();
	}

}
