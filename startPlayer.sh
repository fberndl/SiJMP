#!/bin/bash

#uncomment for remote debugging
#DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y"

CLASSPATH=./bin/:/opt/pi4j/lib/'*':./bin/musicplayer/'*':./bin/musicplayer/



sudo java -Djava.util.logging.config.file=./mylogging.properties $DEBUG -cp $CLASSPATH musicplayer.Main ../music/  

#sudo java -cp $CLASSPATH BlinkGpioExample

