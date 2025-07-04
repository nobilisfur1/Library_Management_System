#!/bin/bash

# Script to compile and run library management program.


javac -cp libs/sqlite-jdbc-3.50.1.0.jar -d out src/**/*.java
java -cp "libs/sqlite-jdbc-3.50.1.0.jar:out" Main

