#!/bin/sh
mvn package
java -Dsun.java2d.opengl=true -Dsun.java2d.opengl.vsync=false -jar target/evolution-simulator-1.0.0.jar