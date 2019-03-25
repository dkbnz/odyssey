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


function hideTable() {
    document.getElementById("keywords").classList.add("d-none");
}


function hideSuccessBanner() {
    window.location.reload();
}


function hideErrorBanner() {
    document.getElementById("createDestinationError").classList.add("hide");
}


function createDestination() {
    var name = document.getElementById("newDest_name").value;
    var type = document.getElementById("newDest_type").value;
    var district = document.getElementById("newDest_district").value;
    var latitude = document.getElementById("newDest_latitude").value;
    var longitude = document.getElementById("newDest_longitude").value;
    var country = document.getElementById("newDest_country").value;

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
            document.getElementById("createDestinationSuccess").classList.remove("hide");
        },
        error: function (error) {
            document.getElementById("createDestinationError").classList.remove("hide");
        }
    });
}
