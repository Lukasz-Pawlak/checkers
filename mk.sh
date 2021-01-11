#!/usr/bin/env bash

echo 'Manifest-Version: 1.0
Main-Class: edu.pwr.checkers.server.ClassicServer' > ./META-INF/MANIFEST.MF ;

cd ./target/classes || (echo "Abort" ; exit);

jar cmf ../.././META-INF/MANIFEST.MF server.jar \
./edu/pwr/checkers/server/* \
./edu/pwr/checkers/model/* \
./edu/pwr/checkers/client/ClientMessage.class \
./edu/pwr/checkers/Logger.class ;

cd ../.. || (echo "Abort" ; exit);

echo 'Manifest-Version: 1.0
Main-Class: edu.pwr.checkers.client.ClassicClient' > ./META-INF/MANIFEST.MF ;

cd ./target/classes || (echo "Abort" ; exit);

jar cmf ../.././META-INF/MANIFEST.MF client.jar \
./edu/pwr/checkers/client/* \
./edu/pwr/checkers/model/* \
./edu/pwr/checkers/Logger.class \
./edu/pwr/checkers/server/ServerMessage.class \
./edu/pwr/checkers/client/view/* ;
