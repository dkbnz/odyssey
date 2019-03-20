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
            dataType: 'json',
            data: JSON.stringify({username : $("#username").val()}),
            error: function (jqXHR) { // "Called if the request fails"
                if (jqXHR.status === 200) {
                    $("#err_username").remove();
                } else if (jqXHR.status === 400) {
                    $('#username_group').append("\n" +
                        "                    <div id=\"err_username\" class=\"alert alert-danger \">\n" +
                        "                        <strong>Username taken!</strong> Please use another username\n" +
                        "                    </div>");
                }
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
        var errors = false;

        if (!($("#password").val() === $("#password_retyped").val())) {
            $("#err_password").show();
            errors = true;
            alert(" PASS ERROR");
        }

        e.preventDefault();
        if (!errors) {
            $("#signUpPopup").modal("hide");
            $("#signUpContinued").modal("show");
            alert("NO ERROR");
        } else {
            alert("ERROR");
        }


    });

});