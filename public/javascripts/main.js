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

    $("#signup-form").submit(function (e) {
        // When initial signup form is submitted, shows next modal
        var errors = false;
        var csrf_token = $('meta[name="csrf-token"]').attr('content');

        $.ajax({
            method: "POST",
            url: "/checkUsername",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({username : $("#username").val()}),
            success: function (response) { // "Called if the request succeeds"
                $("#err_username").hide();
                console.log("Hello")
            },
            error: function () { // "Called if the request fails"
                $("#err_username").show();
                errors = true;
            }
        });

        if (!($("#password") === $("#password_retyped"))) {
            $("#err_password").show();
            errors = true;;
        }

        e.preventDefault();
        if (!errors) {
            $("#signUpPopup").modal("hide");
            $("#signUpContinued").modal("show");
        }

    });

});