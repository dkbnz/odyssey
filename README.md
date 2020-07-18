# Odyssey

![Odyssey Logo](doc/img/full_logo.png)

## About
Odyssey allows you to create and solve quests as you travel around the world, all while earning points and badges for using the various features of the application. You can even solve quests on your mobile devices, allowing you to take Odyssey with you while you solve quests.

## The Team
The developers of Odyssey are a group of 8 students, dubbed Everyware, from the University of Canterbury's [SENG302](https://www.canterbury.ac.nz/courseinfo/GetCourseDetails.aspx?course=SENG302&year=2019) class of 2019.
We wanted to create an application that the university could use for events such as open days.
This application was developed over the course of eight months of university work, so we hope you enjoy it!

See the [User Manual](https://github.com/dkbarrett/odyssey/wiki/User-Manual) for instructions to run the application.

## Dependencies

Everyware's Odyssey requires the following dependencies to run.
It has been tested to run on both Windows 10 and Linux Mint. 

* [sbt 1.2.8](https://www.scala-sbt.org/download.html)
* [nodejs 12.18.2 LTS](https://nodejs.org/en/)
* [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

## Build instructions (Windows/Linux)

```bash
git clone https://github.com/dkbarrett/odyssey.git && cd odyssey # Clone this repository
sbt dist # Build the application
```

The resulting zip file should now be in `./target/universal`

It may be extracted to your preferred install location.

## Run instructions

#### Windows
1. Navigate into the newly extracted `${INSTALL_LOCATION}/bin` folder
2. Execute the `odyssey-X.X.bat` file

#### Linux
1. Navigate into the newly extracted `${INSTALL_LOCATION}/bin` folder
2. Open the folder in the terminal and type `chmod +x ./odyssey-X.X`. This enables the file to be executable. 
3. Execute `./odyssey-X.X`.

Congratulations, Odyssey should now be running!
Your browser should open onto the Odyssey website, but if it does not navigate to `localhost:8080`.  
You can now Create a Profile or Login.  

To login as an admin user use username `admin@travelea.com` and password `1nimda`.  
To login as a regular user use username `guestUser@travelea.com` and password `guest123`.

### User Manual

Click [here](https://github.com/dkbarrett/odyssey/wiki/User-Manual) for a comprehensive guide to using Odyssey.  

### Reference
* [Play Documentation](https://playframework.com/documentation/latest/Home)
* [EBean](https://www.playframework.com/documentation/latest/JavaEbean) is a Java ORM library that uses SQL.The documentation can be found [here](https://ebean-orm.github.io/).

### Licencing
All external licences can be found in the `/doc` directory in the repo or on our Wiki.
