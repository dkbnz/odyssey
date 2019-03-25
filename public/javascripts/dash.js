// All javascript functionality for the dashboard goes in here.

//const getAllProfiles = `SELECT Profile.id, Profile.username, Profile.first_name, Profile.middle_name, Profile.last_name, Profile.gender, Profile.date_of_birth, (SELECT nationality FROM nationality LEFT JOIN profile_nationality ON nationality.id = profile_nationality.nationality_id) FROM profile`

//
// @for(person <- people) {
//     <tr>
//         <td>@person.getIdNumber()</td>
//         <td>@person.getFirstName()</td>
//         <td>@person.getMiddleName()</td>
//         <td>@person.getLastName()</td>
//         <td>@person.getDateOfBirth()</td>
//         <td>@person.getGender()</td>
//         <td>@person.getNationality()</td>
//         <td>@person.getPassportCountry()</td>
//         <td>@person.getTravellerType()</td>
//     </tr>
// }

$(document).ready(function () {

    $.ajax({
        type: 'GET',
        url: "api/profile",
        dataType: 'html',
        success: function (response) {
            $("#profile").append(response)
        },
        error: function (error) {
            console.log(error)
        }
    });

});