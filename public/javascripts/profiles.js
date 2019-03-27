/**
 * Following ajax call populates the nationality dropdowns
 */
$.ajax({
    method: "GET",
    url: "/api/nationalities",
    success: function (response) { // Called if the request succeeds
        for (var key in response) {
            $("#profile-nationality").append("<option value=\"" + response[key].nationality + "\">" + response[key].nationality + "</option>");
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
    error: function (error) { // Called if the request fails
        console.log(error)
    }
});


/**
 * Populates the Traveller Type select container with traveller types from the database on the Profile Search page.
 */
$.ajax({
    method: "GET",
    url: "/api/travtypes",
    success: function (response) { // Called if the request succeeds
        for (var key in response) {
            $("#profile-travellertypes").append("<option value=\"" + response[key].travellerType + "\">" + response[key].travellerType + "</option>");
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
    error: function (error) { // Called if the request fails
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
    var checkAges = checkAge(minAge, maxAge);

    if(checkAges.length === 0 ) {
        $.ajax({
            type: 'GET',
            url: "api/profiles",
            data: {'nationality': nationalities, 'gender': gender, 'min_age': minAge, 'max_age': maxAge, 'traveller_type': travType},
            dataType: 'html',

            success: function(response) {
                $("#keywords").remove();
                $("#tableContent").append(response);
                document.getElementById("keywords").classList.remove("d-none");
                document.getElementById("tableProfiles").classList.add("active");
                var newTableObject = document.getElementById("keywords");
                sorttable.makeSortable(newTableObject);
            },
            error: function(error) {
                console.log(error);
                document.getElementById("keywords").classList.remove("d-none");
            }
        });
    } else {
        timeoutAlert("#searchProfileError", "We found errors in the following field(s):" + checkAges + ". Must be a number");
    }
}

function checkAge(minAge, maxAge) {
    var checkAges = [];
    if(isNaN(minAge)) {
        checkAges.push(" Min Age");
    }
    if (isNaN(maxAge)) {
        checkAges.push(" Max Age");
    }
    return checkAges;
}

/**
 * Function to hide the alert bars after a time. Using the given id of the fields and the text to show in the error.
 * @param id of the alert fields
 * @param text of error, depending on if its a user error or server error.
 */
function timeoutAlert(id, text) {
    console.log(text);
    document.getElementById("searchProfileError").innerHTML = text;
    $(id).fadeTo(5000, 500).slideUp(1000, function(){
        $(id).slideUp(1000);
    });
}

/**
 * Function to hide the error banner when input is valid.
 */
function hideErrorBanner() {
    document.getElementById("searchProfileError").classList.add("hide");
}