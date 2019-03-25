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
                maxHeight: 400

            });

            $('#passport').multiselect({
                buttonWidth: '60%',
                enableFiltering: true,
                maxHeight: 400
            });
        },
        error: function (error) { // "Called if the request fails"
            console.log(error)
        }
    });


    /**
     *  Upon deselecting input container, check the text entry from the input container against the database to see if their proposed username is already taken
     */
    $('#username').focusout(function() {
        $("#err_username").remove();
        $.ajax({
            method: "POST",
            url: "/api/checkUsername",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({username : $("#username").val()}),
            success: function () {
                $("#err_username").remove();
            },
            error: function () {
                $('#username_group').append("\n" +
                        "                    <div id=\"err_username\" class=\"alert alert-danger \">\n" +
                        "                        <strong>Username taken!</strong> Please use another username\n" +
                        "                    </div>");
            }
        });
    });

    $('#password').focusout(function () {
        $("#err_password1").remove();
        var mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");

        if(!(mediumRegex.test($("#password").val()))) {
            $(".p1").append("\n" +
                "                        <div id=\"err_password1\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Your password is weak</strong> You must have at least 2 of the following: lowercase letters, uppercase letters, numbers. Password must also be at least 6 characters long. \n" +
                "                        </div>");
        } else {
            $("#err_password1").remove();
        }
    });



    /**
     * Upon deselecting password input container, check matching password retype to determine error message visibility.
     */
    $('.password').focusout(function() {
        $("#err_password").remove();
        if (!($("#password").val() === $("#password_retyped").val())) {
            $("#password_group").append("\n" +
                "                        <div id=\"err_password\" class=\"alert alert-danger\" >\n" +
                "                            <strong>Passwords do not match!</strong> Please ensure this matches your password!\n" +
                "                        </div>");
        } else {
            $("#err_password").remove();
        }
    });


    /**
     * Upon deselecting password_retyped input container, check matching password retype to determine error message visibility.
     */
    $('#password_retyped').focusout(function() {
        $("#err_password").remove();
        if (!($("#password").val() === $("#password_retyped").val())) {
            $("#password_group").append("\n" +
                "                        <div id=\"err_password\" class=\"alert alert-danger\" >\n" +
                "                            <strong>Passwords do not match!</strong> Please ensure this matches your password!\n" +
                "                        </div>");
        } else {
            $("#err_password").remove();
        }
    });



    /**
     * Upon deselecting first_name input container, check matching password retype to determine error message visibility.
     */
    $("#first_name").focusout(function () {
        $("#err_firstname").remove();

        var nameRegex = new RegExp("^(?=.{1,30}$)[a-zA-Z -']*$");

        if(!(nameRegex.test($("#first_name").val()))) {
            $("#firstname_group").append("\n" +
                "                        <div id=\"err_firstname\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Name is invalid!</strong> The first name must be between 1 and 30 characters and have no numbers or other symbols!\n" +
                "                        </div>");
        } else {
            $("#err_firstname").remove();
        }
    });


    /**
     * Upon deselecting middle_name input container, check matching password retype to determine error message visibility.
     */
    $("#middle_name").focusout(function () {
        $("#err_middlename").remove();

        var nameRegex = new RegExp("^(?=.{1,30}$)[a-zA-Z -']*$");

        if (!(nameRegex.test($("#middle_name").val()))) {
            $("#middlename_group").append("\n" +
                "                        <div id=\"err_middlename\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Invalid name(s)!</strong> The middle name(s) must be between 1 and 30 characters and have no numbers or other symbols!\n" +
                "                        </div>");
        } else {
            $("#err_middlename").remove();
        }
    });


    /**
     * Upon deselecting last_name input container, check matching password retype to determine error message visibility.
     */
    $("#last_name").focusout(function () {
        $("#err_lastname").remove();

        var nameRegex = new RegExp("^(?=.{1,30}$)[a-zA-Z -']*$");

        if(!(nameRegex.test($("#last_name").val()))) {
            $("#lastname_group").append("\n" +
                "                        <div id=\"err_lastname\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Name is invalid!</strong> The last name must be between 1 and 30 characters and have no numbers or other symbols!\n" +
                "                        </div>");
        } else {
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

        $.ajax({
            method: "POST",
            url: "/api/profiles",
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
            },
            error: function (error) { // "Called if the request fails"
                console.log(error)
            }
        });
    });

});