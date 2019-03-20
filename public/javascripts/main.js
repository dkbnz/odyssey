$(document).ready(function () {


    $('#nationality').multiselect({
        enableFiltering: true,
        maxHeight: 400,
        // Add selected nationality to passport dropdown option box
        onChange: function(option, checked) {
            nat_id = $(option).val();//Get the id from the nationality option
            $('#passport').append('<option value="' + nat_id + '">Country ' + nat_id + '</option>');
        }
    });


    $('#passport').multiselect({
        includeSelectAllOption: true,
        maxHeight: 400
    });


    $('#travType').multiselect({
        maxHeight: 400
    });


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


    $("#signup-form").submit(function (e) {
        // When initial signup form is submitted, shows next modal

        e.preventDefault();
        if (($("#err_username").length == 0) && ($("#err_password").length == 0) ) {
            $("#signUpPopup").modal("hide");
            $("#signUpContinued").modal("show");
        } else {
            alert("Please correctly fill out all fields before proceeding.");
        }
    });

});