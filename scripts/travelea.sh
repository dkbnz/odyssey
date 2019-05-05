#!/bin/bash
# Script is now located in /opt/travelea/travelea.sh
# For regression purposes

PROD_OR_DEV=$1

startServer () {
    cd "${DEPLOY_DIR}/${PROD_OR_DEV}/seng302-team-100-everyware-*-SNAPSHOT/bin/"
    case $PROD_OR_DEV in
        production)
            echo Deploying production...
            sudo ./seng302-team-100-everyware -Dhttp.port=443
            ;;
        development)
            echo Deploying development...
            sudo ./seng302-team-100-everyware -Dhttp.port=8443
            ;;
    esac
}

if [ -z "$PROD_OR_DEV" ]
then
    echo "Usage: travelea.sh <production/development>"
else
    startServer
fi