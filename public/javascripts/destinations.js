/**
 * Function to search for destinations, uses an Ajax GET request and populates table in the view.
 */
function searchDestinations() {
    //document.getElementById("tableDestinations").className("tab-pane fade active show");
    var name = document.getElementById("dest_name").value;
    var type = document.getElementById("dest_type").value;
    var district = document.getElementById("dest_district").value;
    var latitude = document.getElementById("dest_latitude").value;
    var longitude = document.getElementById("dest_longitude").value;
    var country = document.getElementById("dest_country").value;

    $.ajax({
        type: 'GET',
        url: "api/destinations",
        data: {'name': name, 'type': type, 'district': district, 'latitude': latitude, 'longitude': longitude, 'country': country},
        dataType: 'html',
        success: function(response) {
            $("#keywords").remove();
            $("#tableContent").append(response);
            document.getElementById("keywords").classList.remove("d-none");
            document.getElementById("tableDestinations").classList.add("active");
            var newTableObject = document.getElementById("keywords");
            sorttable.makeSortable(newTableObject);


        },
        error: function(error) {
            document.getElementById("keywords").classList.remove("d-none");
        }
    });
}

/**
 * Hides the table upon load of the creat destinations page.
 */
function hideTable() {
    var table = document.getElementById("keywords");
    if (table !== null) {
        table.classList.add("d-none");
    }
}

/**
 * Checks if the table exists, if it does it is displayed upon load of the search destinations page.
 */
function showTable() {
    var table = document.getElementById("keywords");
    if (table !== null) {
        table.classList.remove("d-none");
    }
}

/**
 * Function to hide the success banner when input is valid.
 */
function hideSuccessBanner() {
    window.location.reload();
}

/**
 * Function to hide the error banner when input is valid.
 */
function hideErrorBanner() {
    document.getElementById("createDestinationError").classList.add("hide");
}

/**
 * Function to reset the form when destination is created.
 */
function resetForm() {
    document.getElementById("newDest_name").value = "";
    document.getElementById("newDest_district").value = "";
    document.getElementById("newDest_latitude").value = "";
    document.getElementById("newDest_longitude").value = "";
    document.getElementById("newDest_country").value = "";
}

/**
 * Function to create a destination, uses an Ajax POST request and populates the database if input is valid.
 */
function createDestination() {
    var name = document.getElementById("newDest_name").value;
    var type = document.getElementById("newDest_type").value;
    var district = document.getElementById("newDest_district").value;
    var latitude = Number(document.getElementById("newDest_latitude").value);
    var longitude = Number(document.getElementById("newDest_longitude").value);
    var country = document.getElementById("newDest_country").value;

    // Checks the fields are valid, otherwise returns an error.
    if (name.length !==0 && type.length !== 0 && district.length !== 0 && latitude.length !== 0 && longitude.length !== 0 && country.length !== 0 && !isNaN(latitude) && !isNaN(longitude)) {
        $.ajax({
            method: "POST",
            url: "/api/destinations",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                name : name,
                type : type,
                district : district,
                latitude : latitude,
                longitude : longitude,
                country : country
            }),
            success: function () {
                document.getElementById("createDestinationError").className = "banner banner-top alert-danger hide";
                document.getElementById("createDestinationSuccess").classList.remove("hide");
                resetForm();
            },
            error: function (error) {
                document.getElementById("createDestinationSuccess").className = "banner banner-top alert-success hide";
                document.getElementById("createDestinationError").classList.remove("hide");
            }
        });
    } else {
        document.getElementById("createDestinationError").classList.remove("hide");
    }


}