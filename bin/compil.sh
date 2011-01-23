#!/bin/bash
cd src/
javac -d ../bin/ fract/algo/$1.java
echo "Le 1er paramètre est : $1"
