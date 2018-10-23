package musicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple WavFileReader class. 
 * Only lists all wav files of one directory.
 * 
 * 
 * @author Florian Berndl
 */
public abstract class WavFileReader {

	/**
	 * Lists all *.wav files of an directory and returns a List
	 * 
	 * @param directory of the *.wav files
	 * @return returns a list of *.wav files
	 */
	static List<String> ListWavFilesOfDir(String directory) {
		List<String> playlist = new ArrayList<String>();
		File[] paths;

		File dir = new File(directory);
		if (!dir.isDirectory()) {
			System.err.println("Given argument is not a directory!");
			System.exit(0);
		}
		paths = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".wav");
			}
		});

		if (paths.length == 0) {
			System.err.println("No .wav files found!");
			System.exit(0);
		}

		for (File path : paths) {
			playlist.add(path.toString());
		}

		Collections.sort(playlist);

		return playlist;
	}

}
