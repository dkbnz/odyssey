# SENG302 Team 100 - Everyware
See the User Manual inside the project Wiki for how to setup the the application to run.

## How to run using SBT
Either:  
a) Double click on the runTravelEA.sh file in the root directory.  
OR  
b) Open the root directory in terminal and type sbt run.
Upon compile, open your local browser and navigate to localhost:9000

## Extracting using SBT dist
If you are cloning the repository, navigate to the cloned directory in terminal. Then skip to step 4.
1. Download the ZIP Folder from the Repo.
2. Extract the ZIP Folder into a folder.
3. Navigate to the newly extracted folder in terminal.  
4. Type `sbt dist`.
5. Navigate to `team-100-sprint_2.3/target/universal`
6. Extract the `seng302-team-100-everyware-0.0.1-SNAPSHOT` ZIP Folder onto your Desktop or other location.
7. Navigate into the newly extracted `seng302-team-100-everyware-0.0.1-SNAPSHOT/bin` folder in terminal.
8. Right click on `seng302-team-100-everyware` file. Click `Permissions` and click tick the `Allow executing file as a program` button.
9. Double click on the `seng302-team-100-everyware` file.
10. Upon compile, open your local browser and navigate to localhost:9000


Congratulations, TravelEA should now be running, you can now Create a Profile or Login.

### Reference
* [Play Documentation](https://playframework.com/documentation/latest/Home)
* [EBean](https://www.playframework.com/documentation/latest/JavaEbean) is a Java ORM library that uses SQL.The documentation can be found [here](https://ebean-orm.github.io/).
* For Java Forms, Please see [here](<https://playframework.com/documentation/latest/JavaForms>).

### Licencing
All external licences can be found in the `/doc` directory in the repo.