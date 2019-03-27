$(document).ready(function () {

// When logout button clicked, send POST request to logout
    $("#logout-btn").click(function () {
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
