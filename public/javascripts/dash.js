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

            $("#edit-form").submit(function(e) {

                e.preventDefault();

                $.ajax({
                    method: "PUT",
                    url: "/api/profile",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify({
                        username : $("#username").val(),
                        password : $("#password").val(),
                        first_name : $("#first_name").val(),
                        middle_name : $("#middle_name").val(),
                        last_name : $("#last_name").val(),
                        date_of_birth : $("#date_of_birth").val(),
                        gender : $("#gender").val(),
                        nationality : $("#nationality").val(),
                        passport_country : $("#passport").val(),
                        traveller_type : $("input[name='travtypes']:checked").map(function(){return this.value;}).get() // Creates an array of traveller type ids from checked boxes in carousel
                    }),
                    success: function (response) { // "Called if the request succeeds"
                        console.log(response);
                        alert("update successful")
                    },
                    error: function (error) { // "Called if the request fails"
                        console.log(error)
                    }
                });
            });

        },
        error: function (error) {
            console.log(error)
        }
    });


});