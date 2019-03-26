// All javascript functionality for the dashboard goes in here.

//const getAllProfiles = `SELECT Profile.id, Profile.username, Profile.first_name, Profile.middle_name, Profile.last_name, Profile.gender, Profile.date_of_birth, (SELECT nationality FROM nationality LEFT JOIN profile_nationality ON nationality.id = profile_nationality.nationality_id) FROM profile`

$(document).ready(function () {

    $.ajax({
        type: 'GET',
        url: "api/profile",
        dataType: 'html',
        success: function (response) {
            $("#profile").append(response)
        },
        error: function (error) {
            console.log(error)
        }
    });

    $.ajax({
        type: 'GET',
        url: "api/profile/edit",
        dataType: 'html',
        success: function (response) {
            $("#edit").append(response)

            $('#nationality').multiselect({
                buttonWidth: '60%',
                enableFiltering: true,
                maxHeight: 400

            });

            $('#passport').multiselect({
                buttonWidth: '60%',
                enableFiltering: true,
                maxHeight: 400

            });
        },
        error: function (error) {
            console.log(error)
        }
    });

});