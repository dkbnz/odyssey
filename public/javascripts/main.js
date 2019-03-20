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


    $('#username').focusout(function() {
        $("#err_username").remove();
        $.ajax({
            method: "POST",
            url: "/checkUsername",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({username : $("#username").val()}),
            success: function (message) {
                alert("SUCCESS ALERT");
                //$("#err_username").remove();
            },
            error: function () { // "Called if the request fails"

                $('#user_group').append("\n" +
                    "                    <div id=\"err_username\" class=\"alert alert-danger hide\">\n" +
                    "                        <strong>Username taken!</strong> Please use another username\n" +
                    "                    </div>");
            },
        });
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