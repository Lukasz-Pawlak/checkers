#!/usr/bin/env bash

echo 'Manifest-Version: 1.0
Main-Class: edu.pwr.checkers.server.ClassicServer
' > ./META-INF/MANIFEST.MF ;
#./META-INF/MANIFEST.MF \

cd ./target/classes || exit;

jar cmf server.jar \
./edu/pwr/checkers/server/* \
./edu/pwr/checkers/model/* \
./edu/pwr/checkers/Logger.class ;

#echo 'META-INF/MANIFEST.MF
#Manifest-Version: 1.0
#Main-Class: edu.pwr.checkers.client.ClassicClient
#' > META-INF/MANIFEST.MF ;

#jar cf client.jar \
#target/classes/edu/pwr/checkers/client/* \
#target/classes/edu/pwr/checkers/model/* \
#target/classes/edu/pwr/checkers/Logger.class \
#target/classes/edu/pwr/checkers/client/view/* ;

java -jar server.jar
