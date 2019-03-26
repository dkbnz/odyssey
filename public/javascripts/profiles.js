/**
 * Following ajax call populates the passport and nationality dropdowns
 */
$.ajax({
    method: "GET",
    url: "/api/nationalities",
    success: function (response) { // "Called if the request succeeds"
        for (var key in response) {
            $("#profile-nationality").append("<option value=\"" + response[key].id + "\">" + response[key].nationality + "</option>");
        }

        $('#profile-nationality').multiselect({
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



$.ajax({
    method: "GET",
    url: "/api/travtypes",
    success: function (response) { // "Called if the request succeeds"
        for (var key in response) {
            $("#profile-travellertypes").append("<option value=\"" + response[key].id + "\">" + response[key].travellerType + "</option>");
        }

        $('#profile-travellertypes').multiselect({
            buttonWidth: '60%',
            enableFiltering: true,
            maxHeight: 400,
            enableCaseInsensitiveFiltering: true
        });

        $('#profile-gender').multiselect({
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