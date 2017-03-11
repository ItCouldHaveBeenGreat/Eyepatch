#!/bin/bash

echo "Build Started"
scalac -d classes src/*.scala src/*/*.scala -classpath jars/*.jar
if [ $? -eq 0 ]; then
    echo "Build Complete"
    scala -cp classes Runner      
fi