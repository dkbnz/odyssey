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

    $("#signup-form").submit(function (e) {
        // When initial signup form is submitted, shows next modal
        $.ajax({
            type: "POST",
            url: "/checkUsername",
            dataType: "json",
            success: function () {
                // if () {
                // } else {
                // }
            },

            data: JSON.stringify($("#username"))
        });
        e.preventDefault();
        $("#err_username").hide();
        $("#signUpPopup").modal("hide");
        $("#signUpContinued").modal("show");
    });

});