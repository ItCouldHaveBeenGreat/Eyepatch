#!/bin/bash

echo "Build Started"
scalac -d classes src/*.scala src/*/*.scala
if [ $? -eq 0 ]; then
    echo "Build Complete"
    scala -cp classes Runner      
fi