#!/bin/sh
# Compile and run the Campus Smart Parking Simulator (Linux / macOS)
mkdir -p bin
javac -d bin src/mypack/*.java src/*.java && java -cp bin Main "$@"
