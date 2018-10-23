package musicplayer;

import java.util.logging.Level;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceLed;
import com.pi4j.device.piface.PiFaceSwitch;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.wiringpi.Spi;

import devLogger.LoggerWrapper;

/**
 * Raspberry Pi pi4j hardware class which creates button listener and controls leds.
 * 
 * @author Florian Berndl
 * 
 */
public abstract class Pi4jButtonListener {

	static int		cylonSpeed	= 100;
	static PiFace	piface;

	/**
	 * Showing Volume value (0-1) on LED 7-3
	 * 
	 * @param Volume
	 */
	private static void showVolume(double Volume) {
		if (Volume == 1) {
			piface.getLed(PiFaceLed.LED7).on();
			piface.getLed(PiFaceLed.LED6).on();
			piface.getLed(PiFaceLed.LED5).on();
			piface.getLed(PiFaceLed.LED4).on();
			piface.getLed(PiFaceLed.LED3).on();
		} else if (Volume >= 0.8) {
			piface.getLed(PiFaceLed.LED7).on();
			piface.getLed(PiFaceLed.LED6).on();
			piface.getLed(PiFaceLed.LED5).on();
			piface.getLed(PiFaceLed.LED4).on();
			piface.getLed(PiFaceLed.LED3).off();
		} else if (Volume >= 0.6) {
			piface.getLed(PiFaceLed.LED7).on();
			piface.getLed(PiFaceLed.LED6).on();
			piface.getLed(PiFaceLed.LED5).on();
			piface.getLed(PiFaceLed.LED4).off();
			piface.getLed(PiFaceLed.LED3).off();
		} else if (Volume >= 0.4) {
			piface.getLed(PiFaceLed.LED7).on();
			piface.getLed(PiFaceLed.LED6).on();
			piface.getLed(PiFaceLed.LED5).off();
			piface.getLed(PiFaceLed.LED4).off();
			piface.getLed(PiFaceLed.LED3).off();
		} else if (Volume >= 0.2) {
			piface.getLed(PiFaceLed.LED7).on();
			piface.getLed(PiFaceLed.LED6).off();
			piface.getLed(PiFaceLed.LED5).off();
			piface.getLed(PiFaceLed.LED4).off();
			piface.getLed(PiFaceLed.LED3).off();
		} else {
			piface.getLed(PiFaceLed.LED7).off();
			piface.getLed(PiFaceLed.LED6).off();
			piface.getLed(PiFaceLed.LED5).off();
			piface.getLed(PiFaceLed.LED4).off();
			piface.getLed(PiFaceLed.LED3).off();

		}
	}

	/*
	 * Running LEDs up
	 */
	private static void runningLightsUp() {
		// step up the ladder
		for (int index = PiFaceLed.LED3.getIndex(); index <= PiFaceLed.LED7.getIndex(); index++) {
			piface.getLed(index).pulse(cylonSpeed);
			try {
				Thread.sleep(cylonSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Running lights down
	 */
	private static void runningLightsDown() {
		// step down the ladder
		for (int index = PiFaceLed.LED7.getIndex(); index >= PiFaceLed.LED3.getIndex(); index--) {
			piface.getLed(index).pulse(cylonSpeed);
			try {
				Thread.sleep(cylonSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Adds the Switchlistener to the Switches 1-4 of the Piface. Calls the
	 * appropriate musicManager functions
	 * 
	 * musicManager is used by the button listeners
	 * @param musicManager
	 */
	public static void add(MusicManager musicManager) {
		try {

			piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);
			showVolume(musicManager.getVolume());
			// create gpio controller

			// -----------------------------------------------------------------
			// PLAY/PAUSE BUTTON
			// create a button listener for SWITCH #1
			// -----------------------------------------------------------------
			// -- when switch 'S1' is pressed,
			// -- when switch 'S1' is released,
			piface.getSwitch(PiFaceSwitch.S1).addListener(new MusicManagerSwitchListener(musicManager) {
				@Override
				public void onStateChange(SwitchStateChangeEvent event) {
					if (event.getNewState() == SwitchState.ON) {
						LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "[BUTTON S1 PRESSED]");
						switch (mode) {
							case 0:
								musicManager.pauseResume();
								break;
							case 1:
								musicManager.VolumeDown();
								double vol = musicManager.getVolume();
								showVolume(vol);
								break;
							default:
								break;

						}
					}
				}
			});

			// -----------------------------------------------------------------
			// PREV BUTTON
			// create a button listener for SWITCH #2
			// -----------------------------------------------------------------
			// -- when switch 'S2' is pressed,
			// -- when switch 'S2' is released,
			piface.getSwitch(PiFaceSwitch.S2).addListener(new MusicManagerSwitchListener(musicManager) {
				@Override
				public void onStateChange(SwitchStateChangeEvent event) {
					if (event.getNewState() == SwitchState.ON) {
						LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "[BUTTON S2 PRESSED]");
						switch (mode) {
							case 0:
								runningLightsUp();
								musicManager.previousSong();
								break;
							case 1:
								musicManager.VolumeUp();
								double vol = musicManager.getVolume();
								showVolume(vol);
								break;
							default:
								break;
						}
					}
				}
			});

			// -----------------------------------------------------------------
			// NEXT BUTTON
			// create a button listener for SWITCH #3
			// -----------------------------------------------------------------
			// -- when switch 'S3' is pressed, LED02 will start blinking
			// -- when switch 'S3' is released, LED02 will stop blinking and
			// turn
			// OFF
			piface.getSwitch(PiFaceSwitch.S3).addListener(new MusicManagerSwitchListener(musicManager) {
				@Override
				public void onStateChange(SwitchStateChangeEvent event) {
					if (event.getNewState() == SwitchState.ON) {
						LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "[BUTTON S3 PRESSED]");
						switch (mode) {
							case 0:
								runningLightsDown();
								musicManager.nextSong();
								break;
							case 1:
								if (musicManager.loopSong()) {
									piface.getLed(PiFaceLed.LED1).on();
								} else {
									piface.getLed(PiFaceLed.LED1).off();
								}
								break;

							default:
								break;

						}

					}
				}
			});

			// -----------------------------------------------------------------
			// MODE BUTTON
			// create a button listener for SWITCH #4
			// -----------------------------------------------------------------
			// -- when switch 'S4' is pressed, the cylon effect on LED03-LED07
			// will
			// speed up
			// -- when switch 'S4' is pressed, the cylon effect on LED03-LED07
			// will
			// slow down
			piface.getSwitch(PiFaceSwitch.S4).addListener(new MusicManagerSwitchListener(musicManager) {
				@Override
				public void onStateChange(SwitchStateChangeEvent event) {
					if (event.getNewState() == SwitchState.ON) {
						LoggerWrapper.getInstance().getMyLogger().log(Level.FINE, "[BUTTON S4 PRESSED]");
						switch (mode) {
							case 0:
								mode = 1;
								piface.getLed(PiFaceLed.LED0).on();
								break;
							case 1:
								mode = 0;
								piface.getLed(PiFaceLed.LED0).off();
								break;
							default:
								break;

						}
					}
				}
			});

		} catch (Exception e) {
			LoggerWrapper.getInstance().getMyLogger().log(Level.WARNING, "", e);
		}
	}

	private static abstract class MusicManagerSwitchListener implements SwitchListener {
		public MusicManager	musicManager;
		public static Short	mode	= 0;

		public MusicManagerSwitchListener(MusicManager musicManager) {
			this.musicManager = musicManager;

		}
	}
}
