#!/bin/sh
mvn clean compile
mvn package
sleep 2
clear
java -jar target/evolution-simulator-1.0.0.jar