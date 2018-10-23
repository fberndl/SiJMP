# SiJMP
Simple Java Music Player ( Raspberry Pi with external Board)

SJMusicPlayer
Project description:
This simple java music player (SJMusicPlayer) actually supports *.wav files. This project creates button listeners for switch 1-4 and perform led blinking on the Raspberry Pi using the Pi4J library. When a button is pressed the related function of the MusicManager class is called. Unsupported wav files are not skipped. The default folder for music files is "../music", edit the startPlayer.sh script if necessary to play from other folders.

This project includes a simple devLogger class which includes an Console Handler and file handler for debugging purpose for further extensions. Which messages are printed out can be controlled in the mylogging.properties file. e.g. java.util.logging.ConsoleHandler.level = SEVERE The description of the different classes are generated with javadoc and can be found in the generated html documentation or directly in the source code. 

![](rasp.PNG?raw=true)

