package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Profile;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static play.mvc.Results.*;


/**
 * Controller to handle the CRUD of Profiles
 */
public class ProfileController {

    /**
     * Fetches a single profile from the database.
     * If the Id is specified in the JSON request, and the client is an admin, returns specified Id.
     * If the Id is not specified, but the client is logged in, returns client Id
     *
     * @param request HTTP request from client
     * @return HTTP Result of the request
     */
    public Result fetch(Http.Request request) {
        return request.session()
                .getOptional("authorized")
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));

                    JsonNode json = request.body().asJson();

                    if (json.has("idToGet")) {// TODO: Implement admin hierarchy and perform checks here
                        Profile profileToGet = Profile.find.byId(Integer.valueOf(userId));
                        return ok(profileToGet.toJson());
                    } else {
                        // User is just requesting their own profile
                        return ok(userProfile.toJson());
                    }

                })
                .orElseGet(() -> unauthorized("You are not logged in.")); // User is not logged in
    }


//    /**
//     * Checks if the client is an admin, If so then returns an http Result
//     * containing JSON object of all profiles.
//     * Profiles in the database
//     * @param request HTTP request from the client
//     * @return an http ok result if the profiles are listed and client is authorized.
//     * @return an http unauthorized if the client is not admin
//     */
//    public Result list(Http.Request) {
//        //List<Profile> profiles = Profile.find.all();
//        return ok();
//    }
//
//
//    /**
//     * Takes a JSON request and parses it into a Profile object
//     * @param request http request from the client
//     * @return an http ok result
//     */
//    public Result save(Http.Request request) {
//
//        JsonNode json = request.body().asJson();
//
//        Profile existingProfile = Profile.find.query().where()
//                .like("username", json.get("username").asText()).findOne();
//
//        if (existingProfile == null) {
//
//            Profile newProfile = new Profile();
//
//            newProfile.setfName(json.get("fName").asText());
//            newProfile.setmName(json.get("mName").asText());
//            newProfile.setlName(json.get("lName").asText());
//            newProfile.setUsername(json.get("username").asText());
//            newProfile.setPassword(json.get("password").asText());
//            newProfile.setGender(json.get("gender").asText());
//            newProfile.setDateOfBirth(LocalDate.parse(json.get("dateOfBirth").asText()));
//            newProfile.setDateOfCreation(new Date());
//
//            newProfile.save(); // Save profile to create ID
//
//            Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
//                Nationality newNat = Nationality.find.byId(node.asInt());
//                newProfile.addNationality(newNat);
//            };
//            json.get("nationalities").forEach(nationalityAction);
//
//        Consumer<JsonNode> travellerAction = (JsonNode node) -> {
//            TravellerType newTravType = TravellerType.find.byId(node.asInt());
//            newProfile.addTravellerType(newTravType);
//        };
//        json.get("travTypes").forEach(travellerAction);
//
//        Consumer<JsonNode> passportAction = (JsonNode node) -> {
//            Nationality passNat = Nationality.find.byId(node.asInt());
//
//                Passport newPass = new Passport(passNat);
//
//                newPass.profile = newProfile;
//                newPass.save();
//
//                newProfile.addPassport(newPass);
//
//            };
//            json.get("passports").forEach(passportAction);
//
//            newProfile.save();
//
//            return ok("Created");
//        } else {
//            return ok("Username exists");
//        }
//    }
//
//
//    /**
//     * Deletes a profile
//     * @param id of the profile being deleted
//     * @return an http result: notFound if profile doesn't exist, or redirect if the deletion is successful
//     */
//    public Result destroy(Long id) {
//        Profile profile = Profile.find.byId(id.intValue());
//
//        if (profile == null) {
//            return notFound();
//        }
//
//        profile.destroyPassports();
//
//        profile.delete();
//
//        return DELETESUCCESS;
//    }
//
//
//    /**
//     * Displays a profile matching the given username from the HTTP request.
//     * @param username the username of the profile to be displayed
//     * @return the HTTP response containing the new page and profile to be displayed.
//     */
//    public Result retrieveSingle(String username) {
//        Profile profile = Profile.find.query().where()
//                .like("username", username).findOne();
//        return ok(listProfileSingle.render(profile));
//    }
//
//
//    /**
//     * Evaluates the given username and password and checks to see if they match against a stored profile
//     * @param request the HTTP request containing the given username and password.
//     * @return either the login screen or a 'failed to log in' message
//     */
//    public Result attemptLogin(Http.Request request) {
//
//        JsonNode json = request.body().asJson();
//
//        String username = json.get("username").asText();
//        String password = json.get("password").asText();
//
//        // SELECT * FROM profile WHERE username = <username>
//        Profile profile = Profile.find.query().where()
//                .like("username", username).findOne();
//
//        if (profile != null) {
//            if (profile.getPassword().equals(password)) {
//                return ok(listProfileSingle.render(profile));
//            } else {
//                return ok(loginError.render(true));
//            }
//        } else {
//            return ok(loginError.render(false));
//        }
//    }
//
//
//    /**
//     * Renders a view for a profile
//     * @param id of the profile being viewed
//     * @param success a flag dictating what prompt to show
//     * @return an http result: notFound if profile doesn't exist, or ok if the view renders successfully
//     */
//    public Result view(Long id, String success) {
//        Profile profile = Profile.find.byId(id.intValue());
//
//        if (profile == null) {
//            return notFound();
//        }
//
//        return ok(listProfile.render(profile, success));
//    }
//
//
//    /**
//     * Adds a traveller type to a profile
//     * @param request an http request from the client
//     * @return an http result: notFound if profile doesn't exist, or redirect if addition is successful
//     */
//    public Result addTravellerType(Http.Request request) {
//        JsonNode json = req
//    /**
//     * Checks if the client is an admin, If so then returns an http Result
//     * containing JSON object of all profiles.
//     * Profiles in the database
//     * @param request HTTP request from the client
//     * @return an http ok result if the profiles are listed and client is authorized.
//     * @return an http unauthorized if the client is not admin
//     */
//    public Result list(Http.Request) {
//        //List<Profile> profiles = Profile.find.all();
//        return ok();
//    }
//
//
//    /**
//     * Takes a JSON request and parses it into a Profile object
//     * @param request http request from the client
//     * @return an http ok result
//     */
//    public Result save(Http.Request request) {
//
//        JsonNode json = request.body().asJson();
//
//        Profile existingProuest.body().asJson();
//
//
//        Profile profile = Profile.find.byId(json.get("profId").asInt());
//
//        if (profile == null) {
//            return notFound();
//        }
//
//        TravellerType travType = TravellerType.find.byId(json.get("type").asInt());
//        profile.addTravellerType(travType);
//        profile.save();
//
//        return Results.redirect(routes.ProfileController.view(json.get("profId").asLong(), "4"));
//    }
//
//
//    /**
//     * Removes a traveller type from a profile
//     * @param id of the profile having a traveller type removed
//     * @param travId the id of the traveller type being removed
//     * @return an http result: notFound if profile doesn't exist, or redirect if removal is successful
//     */
//    public Result removeTravellerType(Long id, Long travId) {
//
//        Profile profile = Profile.find.byId(id.intValue());
//
//        if (profile == null) {
//            return notFound();
//        }
//
//        TravellerType travType = TravellerType.find.byId(travId.intValue());
//        profile.removeTravellerType(travType);
//        profile.save();
//
//        return Results.redirect(routes.ProfileController.view(id, "3"));
//    }
}
