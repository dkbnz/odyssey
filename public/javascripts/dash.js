// All javascript functionality for the dashboard goes in here.

//const getAllProfiles = `SELECT Profile.id, Profile.username, Profile.first_name, Profile.middle_name, Profile.last_name, Profile.gender, Profile.date_of_birth, (SELECT nationality FROM nationality LEFT JOIN profile_nationality ON nationality.id = profile_nationality.nationality_id) FROM profile`