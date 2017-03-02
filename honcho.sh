#!/bin/bash

echo "Build Started"
FAILURE="$(scalac -d classes src/*.scala src/*/*.scala)"
if [ "${FAILURE}" = "" ]; then
    echo "Build Complete"
    scala -cp classes Runner      
fi