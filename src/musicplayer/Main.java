package musicplayer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;

import devLogger.LoggerWrapper;

/**
 * 
 * This project creates button listeners for switch 1-4 and perform led blinking
 * on the Raspberry Pi using the Pi4J library and plays wav sound files. When a button is pressed the
 * related function of the MusicManager class is called. This simple java music player (SJMusicPlayer)
 * actually supports *.wav files. Unsupported wav files are not skipped.
 
 * @author Florian Berndl
 */

public class Main {

    

    public static void main(String args[]) throws InterruptedException,
	    IOException {

	System.out
		.println("     _______.       __  .___  ___.  __    __       _______. __    ______    .______    __          ___   ____    ____  _______ .______      \r\n"
			+ "    /       |      |  | |   \\/   | |  |  |  |     /       ||  |  /      |   |   _  \\  |  |        /   \\  \\   \\  /   / |   ____||   _  \\     \r\n"
			+ "   |   (----`      |  | |  \\  /  | |  |  |  |    |   (----`|  | |  ,----'   |  |_)  | |  |       /  ^  \\  \\   \\/   /  |  |__   |  |_)  |    \r\n"
			+ "    \\   \\    .--.  |  | |  |\\/|  | |  |  |  |     \\   \\    |  | |  |        |   ___/  |  |      /  /_\\  \\  \\_    _/   |   __|  |      /     \r\n"
			+ ".----)   |   |  `--'  | |  |  |  | |  `--'  | .----)   |   |  | |  `----.   |  |      |  `----./  _____  \\   |  |     |  |____ |  |\\  \\----.\r\n"
			+ "|_______/     \\______/  |__|  |__|  \\______/  |_______/    |__|  \\______|   | _|      |_______/__/     \\__\\  |__|     |_______|| _| `._____|");


	System.out.println("\n\n Short guide:\n"
			+ "#####################################################################\r\n"
			+ "#   Switch 1           #  Switch 2     #  Switch 3    # Switch 4    #\r\n"         
			+ "# Mode 0: Play/Pause   # Previous song # Next Song    # Mode Select #\r\n"
			+ "# Mode 1: Volume up    # Volume down   # Loop Song    #             #\r\n"  
			+ "#####################################################################\r\n");
	

	
	
	//Start MusicManager
	List <String> myPlaylist =  WavFileReader.ListWavFilesOfDir(args[0]);
	final MusicManager musicManager = new MusicManager(myPlaylist, new BigDecimal(0.2D));
	Pi4jButtonListener.add(musicManager);
	musicManager.nextSong();

	
	// run continuously until user aborts with CTRL-C
	try {
	    while (true) {
	    	//here foreground actions can be performed!
	    }
	} catch (RejectedExecutionException e) {
	    LoggerWrapper.getInstance().getMyLogger().log(Level.INFO, "", e);
	}

    }

}
