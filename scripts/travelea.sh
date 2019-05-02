#!/bin/bash
start () {
    cd /var/lib/gitlab-runner/builds/69a828c9/0/seng302-2019/team-100/target/universal/seng302-team-100-everyware-0.0.1-SNAPSHOT/bin/
    case $1 in
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

case $2 in
    start)
        start $1
        ;;
esac