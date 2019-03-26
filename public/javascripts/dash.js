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




            /**
             *  Upon deselecting input container, check contents against the database to see if their proposed username is already taken
             */
            $('#username').focusout(function() {
                $("#err_username").remove();
                $.ajax({
                    method: "POST",
                    url: "/api/checkUsername",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify({username : $("#username").val()}),

                    success: function () {
                        // Profile controller method has verified input uniqueness returning ok(200), so existing error messages should be removed.
                        $("#err_username").remove();
                    },

                    error: function () {
                        // Profile controller method has detecting an existing username matching the input returning badRequest(400), so an error message is presented.
                        $('#username_group').append("\n" +
                            "                    <div id=\"err_username\" class=\"alert alert-danger \">\n" +
                            "                        <strong>Username taken!</strong> Please use another username\n" +
                            "                    </div>");
                    }
                });
            });


            /**
             * Upon deselecting password input container, check if the contents match the regex to determine validity.
             */
            $('#password').focusout(function () {
                $("#err_password1").remove();
                var mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");

    // When logout button clicked, send POST request to logout
    $("#logout-btn").click(function(){
        $.ajax({
            method: "POST",
            url: "/api/logout",
            success: function (response) { // "Called if the request succeeds"
                window.location = "/" // Redirect to index
            },
            error: function (error) { // "Called if the request fails"
                console.log(error)
            }
        });
    });

});
                if(!(mediumRegex.test($("#password").val()))) {
                    // If the contents checked are not accepted by the regex, then present an error message below the container.
                    $(".p1").append("\n" +
                        "                        <div id=\"err_password1\" class=\"alert alert-danger\" > \n" +
                        "                            <strong>Your password is weak</strong> You must have at least 2 of the following: lowercase letters, uppercase letters, numbers. Password must also be at least 6 characters long. \n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_password1").remove();
                }
            });


            /**
             * Upon deselecting password input container, check matching password retype to determine error message visibility.
             */
            $('.password').focusout(function() {
                $("#err_password").remove();
                if (!($("#password").val() === $("#password_retyped").val())) {
                    // If the password and its retype do not match, then present an error message below the container
                    $("#password_group").append("\n" +
                        "                        <div id=\"err_password\" class=\"alert alert-danger\" >\n" +
                        "                            <strong>Passwords do not match!</strong> Please ensure this matches your password!\n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_password").remove();
                }
            });


            /**
             * Upon deselecting password_retyped input container, check matching password retype to determine error message visibility.
             */
            $('#password_retyped').focusout(function() {
                $("#err_password").remove();
                if (!($("#password").val() === $("#password_retyped").val())) {
                    // If the password and its retype do not match, then present an error message below the container
                    $("#password_group").append("\n" +
                        "                        <div id=\"err_password\" class=\"alert alert-danger\" >\n" +
                        "                            <strong>Passwords do not match!</strong> Please ensure this matches your password!\n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_password").remove();
                }
            });


            /**
             * Upon deselecting first_name input container, check if contents are valid according to a regular expression.
             */
            $("#first_name").focusout(function () {
                $("#err_firstname").remove();

                var nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");

                if(!(nameRegex.test($("#first_name").val()))) {
                    // If the contents checked are not accepted by the regex, then present an error message below the container.
                    $("#firstname_group").append("\n" +
                        "                        <div id=\"err_firstname\" class=\"alert alert-danger\" > \n" +
                        "                            <strong>Invalid!</strong> Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_firstname").remove();
                }
            });


            /**
             * Upon deselecting middle_name input container, check if contents are valid according to a regular expression.
             */
            $("#middle_name").focusout(function () {
                $("#err_middlename").remove();

                var nameRegex = new RegExp("(^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$)|^$");

                if (!(nameRegex.test($("#middle_name").val()))) {
                    // If the contents checked are not accepted by the regex, then present an error message below the container.
                    $("#middlename_group").append("\n" +
                        "                        <div id=\"err_middlename\" class=\"alert alert-danger\" > \n" +
                        "                            <strong>Invalid!</strong> Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_middlename").remove();
                }
            });


            /**
             * Upon deselecting last_name input container, check if contents are valid according to a regular expression.
             */
            $("#last_name").focusout(function () {
                $("#err_lastname").remove();

                var nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");

                if(!(nameRegex.test($("#last_name").val()))) {
                    // If the contents checked are not accepted by the regex, then present an error message below the container.
                    $("#lastname_group").append("\n" +
                        "                        <div id=\"err_lastname\" class=\"alert alert-danger\" > \n" +
                        "                            <strong>Invalid!</strong> Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n" +
                        "                        </div>");
                } else {
                    // Otherwise the contents are valid and any error messages present should be removed.
                    $("#err_lastname").remove();
                }
            });



            $("#edit-form").submit(function(e) {

                e.preventDefault();


                // Perform traveller type and nationality checks here
                if($("input[name='travtypes']:checked").map(function(){return this.value;}).get().length == 0) {
                    alert("Please select at least 1 traveller type")
                } else {

                    $.ajax({
                        method: "PUT",
                        url: "/api/profile",
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify({
                            username: $("#username").val(),
                            password: $("#password").val(),
                            first_name: $("#first_name").val(),
                            middle_name: $("#middle_name").val(),
                            last_name: $("#last_name").val(),
                            date_of_birth: $("#date_of_birth").val(),
                            gender: $("#gender").val(),
                            nationality: $("#nationality").val(),
                            passport_country: $("#passport").val(),
                            traveller_type: $("input[name='travtypes']:checked").map(function () {
                                return this.value;
                            }).get() // Creates an array of traveller type ids from checked boxes in carousel
                        }),
                        success: function (response) { // "Called if the request succeeds"
                            console.log(response);
                            alert("update successful")
                        },
                        error: function (error) { // "Called if the request fails"
                            console.log(error)
                        }
                    });
                }

            });

        },
        error: function (error) {
            console.log(error)
        }
    });


});