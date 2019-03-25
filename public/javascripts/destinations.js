function searchDestinations() {
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
        },
        error: function(error) {
            console.log("ERROR");
            document.getElementById("keywords").classList.remove("d-none");
            console.log(error);
        }
    });
}