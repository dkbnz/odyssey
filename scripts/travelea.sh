#!/bin/bash

start () {
    cd ../target/universal/seng302-team-100-everyware-0.0.1-SNAPSHOT/bin
    case $1 in
        production)
            echo Production
            ;;
        development)
            echo Deploying development...
            ./seng302-team-100-everyware -Dhttp.port=8443
            ;;
esac
}

case $2 in
    start)
        start
        ;;
esac