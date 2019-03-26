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

        /*
        $('#profile-nationality').multiselect({
            buttonWidth: '60%',
            enableFiltering: true,
            maxHeight: 400,
            enableCaseInsensitiveFiltering: true

        });
        */

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

        /*
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
        */


    },
    error: function (error) { // "Called if the request fails"
        console.log(error)
    }
});

/**
 * Function to search for destinations, uses an Ajax GET request and populates table in the view.
 */
function searchProfiles() {
    var nationalities = document.getElementById("profile-nationality").value;
    var gender = document.getElementById("profile-gender").value;
    var minAge = document.getElementById("profile-min-age").value;
    var maxAge = document.getElementById("profile-max-age").value;
    var travType = document.getElementById("profile-travellertypes").value;

    console.log(nationalities, gender, minAge, maxAge, travType);

    $.ajax({
        type: 'GET',
        url: "api/profiles",
        data: {'nationality': nationalities, 'gender': gender, 'min_age': minAge, 'max_age': maxAge, 'traveller_type': travType},
        dataType: 'html',

        success: function(response) {
            console.log("HERE");
            $("#keywords").remove();
            $("#tableContent").append(response);
            document.getElementById("keywords").classList.remove("d-none");
            document.getElementById("tableProfiles").classList.add("active");
            var newTableObject = document.getElementById("keywords");
            sorttable.makeSortable(newTableObject);
        },
        error: function() {
            document.getElementById("keywords").classList.remove("d-none");
        }
    });
}
