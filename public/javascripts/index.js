/**
 * Hides the navbar and the footer on the welcome page.
 */

window.onload = function() {
    document.getElementById('navbar').style.display = 'none';
    document.getElementById('footer').style.display = 'none';

};

$(document).ready(function () {

    /**
     * Following ajax call populates the Traveller type carousel in profile creation
     */
    $.ajax({
        method: "GET",
        url: "/api/travtypes",
        success: function (response) { // Called if the request succeeds

            // Iterator through each traveller type and add to carousel
            for (var key in response) {
                var index = response[key].id-1;
                $("#trav-carousel .carousel-indicators").append("<li data-target=\"#trav-carousel\" data-slide-to=\"" + index + "\"></li>");
                $("#trav-carousel-body").append("<div class=\"carousel-item\">\n" +
                    "  <img style=\"width: 100%\"src=\"" + response[key].imgUrl + "\" alt=\"...\">\n" +
                    "  <div class=\"carousel-caption\">\n" +
                    "    <h3>" +  response[key].travellerType + "</h3>" +
                    "    <h5><input name=\"travtypes\" type=\"checkbox\" value=\"" + response[key].id + "\"> </h5>\n" +
                    "    <p>" + response[key].description + "</p>\n" +
                    "  </div>\n" +
                    "</div>");
            }

            //Set first carousel indicator and first carousel image to active.
            $("#trav-carousel .carousel-indicators li").first().addClass("active");
            $("#trav-carousel-body .carousel-item").first().addClass("active");
        },
        error: function (error) { // Called if the request fails
            console.log(error)
        }
    });

    /**
     * Following ajax call populates the passport and nationality dropdowns
     */
    $.ajax({
        method: "GET",
        url: "/api/nationalities",
        success: function (response) { // "Called if the request succeeds"
            for (var key in response) {
                $("#nationality").append("<option value=\"" + response[key].id + "\">" + response[key].nationality + "</option>");
                $("#passport").append("<option value=\"" + response[key].id + "\">" + response[key].country + "</option>");
            }

            $('#nationality').multiselect({
                buttonWidth: '60%',
                enableFiltering: true,
                maxHeight: 400,
                enableCaseInsensitiveFiltering: true

            });

            $('#passport').multiselect({
                buttonWidth: '60%',
                enableFiltering: true,
                maxHeight: 400,
                enableCaseInsensitiveFiltering: true
            });
        },
        error: function (error) { // "Called if the request fails"
            console.log(error)
        }
    });

    /**
     * Send an empty login request to the server. Should redirect to dashboard if logged in already
     */
    $.ajax({
        method: "POST",
        url: "/api/login",
        success: function () {  // Success send in response if already logged in
            window.location = "/dash"
        },
        error: function () {
        }
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



    /**
     * Checks the validity of uer entries before proceeding to the next section.
     * Alerts the user if invalid entries remain.
     */
    $("#signup-form").submit(function (e) {
        // When initial signup form is submitted, shows next modal

        e.preventDefault();
        if (($("#err_username").length === 0) &&
            ($("#err_password").length === 0) &&
            ($("#err_firstname").length === 0) &&
            ($("#err_middlename").length === 0) &&
            ($("#err_lastname").length === 0)) {

            $("#signUpPopup").modal("toggle");
            $("#signUpContinued").modal("toggle");

        } else {
            alert("Please correctly fill out all fields before proceeding.");
        }

    });

    /**
     * Profile creation handler
     */
    $("#create-form").submit(function(e) {

        e.preventDefault();

        // Perform traveller type and nationality checks here
        if($("input[name='travtypes']:checked").map(function(){return this.value;}).get().length == 0) {
            alert("Please select at least 1 traveller type")
        } else {

            $.ajax({
                method: "POST",
                url: "/api/profiles",
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
                    console.log(response); // TODO: Upon profile successfully being created, redirect to the profile page.
                    window.location = "dash";
                },
                error: function (error) { // "Called if the request fails"
                    console.log(error)
                }
            });

        }
    });
    /**
     *  Profile sign in handler
     */
    $("#sign-in-form").submit(function (e) {
        e.preventDefault();

        $.ajax({
            method: "POST",
            url: "/api/login",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                username: $("#sign-in-username").val(),
                password: $("#sign-in-password").val()
            }),
            success: function(response) {
                window.location = "/dash";  // Direct to dashboard
            },
            error: function(error) {
                // if element exists a length value is returned
                if (!$("#err_sign_in").length) {    // If no length value returned, create warning
                    $("#sign-in-form").append("\n" +
                        "<div id=\"err_sign_in\" class=\"alert alert-danger\" > \n" +
                        "Username or Password Incorrect \n" +
                        "</div>");
                }
            }
        });
    }).focusout(function () {
        $("#err_sign_in").remove();
    });

});