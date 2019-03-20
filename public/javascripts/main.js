$(document).ready(function () {

    $("#create-form").submit(function(e) {
        $.ajax({
            method: "POST",
            url: "/profiles",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                username : $("#username").val(),
                password : $().val(),
                first_name : $().val(),
                middle_name : $().val(),
                last_name : $().val(),
                date_of_birth : $().val(),
                gender : $().val(),
                nationality : $().val(),
            }),
            success: function (response) { // "Called if the request succeeds"
                $("#err_username").hide();
                console.log("Hello")
            },
            error: function () { // "Called if the request fails"
                $("#err_username").show();
                errors = true;
            }
        });
    })

    $('#nationality').multiselect({
        enableFiltering: true,
        maxHeight: 400,
        // Add selected nationality to passport dropdown option box
        onChange: function(option, checked) {
            nat_id = $(option).val();//Get the id from the nationality option
            $('#passport').append('<option value="' + nat_id + '">Country ' + nat_id + '</option>');
        }
    });

    // $('#passport').multiselect({
    //     includeSelectAllOption: true,
    //     maxHeight: 400
    // });
    //
    // $('#travType').multiselect({
    //     maxHeight: 400
    // });


    /**
     *  Upon deselecting input container, check the text entry from the input container against the database to see if their proposed username is already taken
     */
    $('#username').focusout(function() {
        $("#err_username").remove();
        $.ajax({
            method: "POST",
            url: "/checkUsername",
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


    /**
     * Upon deselecting password input container, check matching password retype to determine error message visibility.
     */
    $('#password').focusout(function() {
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

        var nameRegex = new RegExp("^[a-zA-Z]+((-|')[a-zA-Z]+)*$");

        if(!(nameRegex.test($("#first_name").val()))) {
            $("#firstname_group").append("\n" +
                "                        <div id=\"err_firstname\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Name is invalid!</strong> Names must have no spaces, unenclosed hyphens/apostrophes, or numbers!\n" +
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

        var nameRegex = new RegExp("^[a-zA-Z]*((-|')[a-zA-Z]*)*$"); //TODO: change this regex/function to allow null, "", or follow the other regex templates.

        if (!(nameRegex.test($("#middle_name").val()))) {
            $("#middlename_group").append("\n" +
                "                        <div id=\"err_middlename\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Name is invalid!</strong> Names must have no spaces, unenclosed hyphens/apostrophes, or numbers!\n" +
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

        var nameRegex = new RegExp("^[a-zA-Z]+((-|')[a-zA-Z]+)*$");

        if(!(nameRegex.test($("#last_name").val()))) {
            $("#lastname_group").append("\n" +
                "                        <div id=\"err_lastname\" class=\"alert alert-danger\" > \n" +
                "                            <strong>Name is invalid!</strong> Names must have no spaces, unenclosed hyphens/apostrophes, or numbers!\n" +
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
            $("#signUpPopup").modal("hide");
            $("#signUpContinued").modal("show");
        } else {
            alert("Please correctly fill out all fields before proceeding.");
        }
    });

});