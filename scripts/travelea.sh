#!/bin/bash

start () {
    echo $(ls)
}

case $1 in
    start)
        start
        ;;
esac