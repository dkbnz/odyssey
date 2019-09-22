rm -rf output/photos/
mkdir output/photos
mkdir output/photos/thumbnail
python3 users.py

mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < profile.sql
mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < nationality_profile.sql
mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < passport_profile.sql
mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < personal_photo.sql
mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < photo.sql
mysql -u seng302-team100 -h mysql2.csse.canterbury.ac.nz -P 3306 -pMoppetsDenis7291 seng302-2019-team100-test < traveller_type_profile.sql

scp -r /home/cosc/student/dba62/IdeaProjects/team-100/scripts/populate/output/photos/* sengstudent@csse-s302g1:/home/sengstudent/travelea/development/seng302-team-100-everyware-0.0.1-SNAPSHOT/photos
