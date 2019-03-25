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
            console.log(response);
            document.getElementById("keywords").classList.remove("d-none");
            document.getElementById("tableDestinations").classList.add("active");
        },
        error: function(error) {
            console.log("ERROR");
            document.getElementById("keywords").classList.remove("d-none");
            console.log(error);
        }
    });
}


function hideTable() {
    document.getElementById("keywords").classList.add("d-none");
}


function createDestination() {
    var name = document.getElementById("newDest_name").value;
    var type = document.getElementById("newDest_type").value;
    var district = document.getElementById("newDest_district").value;
    var latitude = document.getElementById("newDest_latitude").value;
    var longitude = document.getElementById("newDest_longitude").value;
    var country = document.getElementById("newDest_country").value;
    console.log(name, type, district, latitude, longitude, country);

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
        success: function (response) { // "Called if the request succeeds"
        },
        error: function (error) { // "Called if the request fails"
            console.log(error)
        }
    });
}
