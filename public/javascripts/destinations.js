/**
 * Function to search for destinations, uses an Ajax GET request and populates table in the view.
 */
function searchDestinations() {
    var name = document.getElementById("dest_name").value;
    var type = document.getElementById("searchDestinationTypeSelector").value;
    console.log(type);
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
        error: function() {
            document.getElementById("keywords").classList.remove("d-none");
        }
    });
}

/**
 * Function to create a destination, uses an Ajax POST request and populates the database if input is valid.
 */
function createDestination() {
    var name = document.getElementById("newDest_name").value;
    var type = document.getElementById("newDestinationTypeSelector").value;
    var district = document.getElementById("newDest_district").value;
    var latitude = document.getElementById("newDest_latitude").value;
    var longitude = document.getElementById("newDest_longitude").value;
    var country = document.getElementById("newDest_country").value;

    var fieldList = [name, type, district, latitude, longitude, country];
    var errorList = checkFields(fieldList);

    if(errorList.length === 0) {
        $.ajax({
            method: "POST",
            url: "/api/destinations",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                name : name,
                type : type,
                district : district,
                latitude : Number(latitude),
                longitude : Number(longitude),
                country : country
            }),
            success: function () {
                $('#createDestinationError').hide();
                timeoutAlert("#createDestinationSuccess","");
                resetForm();
            },
            error: function (error) {
                console.log(error);
                timeoutAlert("#createDestinationError", "Internal Server Error, try again! Hint: the destination might already exist");
            }
        });
    } else {
        timeoutAlert("#createDestinationError", "We found errors in the following fields:" + errorList);
    }
}

/**
 * Hides the table upon load of the create destinations page/div.
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
 * Function to reset the form when destination is successfully created.
 */
function resetForm() {
    document.getElementById("newDest_name").value = "";
    document.getElementById("newDest_district").value = "";
    document.getElementById("newDest_latitude").value = "";
    document.getElementById("newDest_longitude").value = "";
    document.getElementById("newDest_country").value = "";
}

/**
 * Function to check all the fields put into the Create Destination form.
 * @param fields
 */
function checkFields(fields) {
    var error = [];
    var possibleFields = [" Name", " Type", " District", " Latitude", " Longitude", " Country"];
    for (var i = 0; i < fields.length; i++) {
        if (fields[i].length === 0) {
            error.push(possibleFields[i]);
        }
    }
    //console.log(fields[3]);
    //console.log(fields[4]);
    if(isNaN(fields[3])) {
        error.push(possibleFields[3]);
    }
    if  (isNaN(fields[4])) {
        error.push(possibleFields[4]);
    }
    return error;
}

/**
 * Function to hide the alert bars after a time. Using the given id of the fields and the text to show in the error.
 * @param id of the alert fields
 * @param text of error, depending on if its a user error or server error.
 */
function timeoutAlert(id, text) {
    document.getElementById("createDestinationError").innerHTML = "Error creating destination! " + text;
    $(id).fadeTo(5000, 500).slideUp(1000, function(){
        $(id).slideUp(1000);
    });
}


