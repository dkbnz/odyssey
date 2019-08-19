package controllers.quests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.ebean.ExpressionList;
import models.ApiError;
import models.Profile;
import models.objectives.Objective;
import models.quests.Quest;
import org.springframework.core.env.Profiles;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.quests.QuestRepository;
import util.AuthenticationUtil;
import util.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static play.mvc.Results.*;
import static util.QueryUtil.queryComparator;

public class QuestController {

    private QuestRepository questRepository;
    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;

    private final String TITLE = "title";
    private final String OPERATOR = "operator";
    private final String OBJECTIVE = "objective";
    private final String FIRSTNAME = "first_name";
    private final String LASTNAME = "last_name";
    private final String COUNTRY = "country";

    @Inject
    public QuestController(QuestRepository questRepository,
                           ProfileRepository profileRepository,
                           DestinationRepository destinationRepository) {
        this.questRepository = questRepository;
        this.profileRepository = profileRepository;
        this.destinationRepository = destinationRepository;
    }


    /**
     * Creates and saves a new objective for a user, checking if the user is creating one for themselves or if
     * the user is an admin. It also checks the request for validity.
     *
     * @param request   the Http request containing a Json body of the new quest details.
     * @param userId    the id of the user who will own the created quest.
     * @return          created() (Http 201) response if creation is successful.
     *                  notFound() (Http 404) response if a quest owner profile cannot be retrieved.
     *                  forbidden() (Http 403) response if the user creating the quest is doing so incorrectly.
     *                  badRequest() (Http 400) response if the request contains any errors in its form or contents.
     *                  unauthorised() (Http 401) response if creation is being attempted while logged out of the app.
     */
    public Result create(Http.Request request, Long userId) {

        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile questOwner = profileRepository.findById(userId);

        if (questOwner == null) {
            return notFound(ApiError.notFound());
        }

        if (!AuthenticationUtil.validUser(loggedInUser, questOwner)) {
            return forbidden(ApiError.forbidden());
        }

        ObjectMapper mapper = new ObjectMapper();

        Quest newQuest;

        try {
            newQuest = mapper.readerWithView(Views.Owner.class)
                    .forType(Quest.class)
                    .readValue(request.body().asJson());
        } catch (IOException e) {
            return badRequest(ApiError.invalidJson());
        }

        newQuest.setOwner(questOwner);
        for(Objective newObjective : newQuest.getObjectives()) {
            newObjective.setOwner(questOwner);
        }
        Collection<ApiError> questCreationErrors = newQuest.getErrors();

        if (!questCreationErrors.isEmpty()) {
            return badRequest(Json.toJson(questCreationErrors));
        }

        for(Objective objective : newQuest.getObjectives()) {
            objective.setDestination(destinationRepository.findById(objective.getDestination().getId()));
        }


        questRepository.save(newQuest);
        profileRepository.update(questOwner);

        questRepository.refresh(newQuest);

        return created(Json.toJson(newQuest));
    }


    /**
     * Edits a specific request from the database if the user is permitted to. Restricts editing if the quest is in use.
     *
     * @param request       the request from the front end of the application containing login information.
     * @param questId       the id of the quest being edited.
     * @return              ok() (Http 200) if the quest has successfully been edited.
     *                      forbidden() (Http 403) if the user is not authorised to edit the quest.
     *                      notFound() (Http 404) if the quest doesn't exist.
     *                      badRequest() (Http 400) if the owner of the quest doesn't exist, or there is an error in the
     *                      Json body of the quest.
     */
    public Result edit(Http.Request request, Long questId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Quest quest = questRepository.findById(questId);

        if (quest == null) {
            return notFound(ApiError.notFound());
        }

        Profile questOwner = quest.getOwner();

        if (!AuthenticationUtil.validUser(loggedInUser, questOwner)) {
            return forbidden(ApiError.forbidden());
        }

        if (questOwner == null) {
            return badRequest(ApiError.notFound());
        }
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Attempt to turn json body into a objective object.
            quest = mapper.readerWithView(Views.Owner.class)
                    .forType(Quest.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            return badRequest(ApiError.invalidJson());
        }

        quest.setOwner(questOwner);
        quest.setId(questId);

        for(Objective newObjective : quest.getObjectives()) {
            newObjective.setOwner(questOwner);
            if (newObjective.getDestination().getId() == null) {
                return badRequest(ApiError.invalidJson());
            }
            newObjective.setDestination(destinationRepository.findById(newObjective.getDestination().getId()));
        }

        Collection<ApiError> questEditErrors = quest.getErrors();

        if (!questEditErrors.isEmpty()) {
            return badRequest(Json.toJson(questEditErrors));
        }

        questRepository.update(quest);

        return ok(Json.toJson(quest));
    }


    /**
     * Deletes a specific quest from the database if the user is permitted to.
     *
     * @param request   the request from the front end of the application containing login information.
     * @param questId   the id of the quest being deleted.
     * @return          ok() (Http 200) response for a successful deletion.
     *                  notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                  forbidden() (Http 403) response containing an ApiError for disallowed deletion.
     *                  badRequest() (Http 400) response containing an ApiError for an invalid Json body.
     *                  unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     */
    public Result delete(Http.Request request, Long questId) {
         Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
         if (loggedInUser == null) {
             return unauthorized(ApiError.unauthorized());
         }

         Quest quest = questRepository.findById(questId);

         if (quest == null) {
             return notFound(ApiError.notFound());
         }

         Profile questOwner = quest.getOwner();

         if (!AuthenticationUtil.validUser(loggedInUser, questOwner)) {
             return forbidden(ApiError.forbidden());
         }

         if (questOwner == null) {
             return badRequest(ApiError.notFound());
         }

         quest.clearObjectives();
         questRepository.update(quest);

         questRepository.delete(quest);
         profileRepository.update(questOwner);
         return ok();
    }


    /**
     * Retrieves all the quests stored in the database where today's date and time is between the quest's start and end
     * dates.
     *
     * @param request   the request from the front end of the application containing login information.
     * @return          ok() (Http 200) response containing a Json body of the retrieved quests.
     *                  badRequest() (Http 400) response containing an ApiError for an invalid Json body.
     *                  unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     */
    public Result fetchAll(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        List<Quest> questQuery = getQuestsQuery(request);
        Calendar now = Calendar.getInstance();

        List<Quest> quests = new ArrayList<>();

        for (Quest quest: questQuery) {
            if ((quest.getStartDate().before(now.getTime())
                    || quest.getStartDate().compareTo(now.getTime()) == 0)
                    && (quest.getEndDate().after(now.getTime())
                    || quest.getEndDate().compareTo(now.getTime()) == 0)) {
                quests.add(quest);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String result;
        try {
            result = mapper
                    .writerWithView(Views.Public.class)
                    .writeValueAsString(quests);
        } catch (JsonProcessingException e) {
            return badRequest(ApiError.invalidJson());
        }

        return ok(result);
    }


    /**
     * Retrieves all the quests owned by a specific user.
     *
     * @param request   the request from the front end of the application containing login information.
     * @param ownerId   the id of the specific user whose quests are being retrieved.
     * @return          ok() (Http 200) response containing the quests owned by the specified user.
     *                  notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                  forbidden() (Http 403) response containing an ApiError for disallowed retrieval.
     *                  unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     */
    public Result fetchByOwner(Http.Request request, Long ownerId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(ownerId);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        if (!AuthenticationUtil.validUser(loggedInUser, requestedUser)) {
            return forbidden(ApiError.forbidden());
        }

        return ok(Json.toJson(requestedUser.getMyQuests()));
    }



    /**
     * Retrieves all the profiles that have the specified the quest currently active
     *
     * @param request   the request from the front end of the application containing login information.
     * @param questId   the id of the quest that the active profiles are being retrieved for
     * @return          ok() (Http 200) response containing the quests owned by the specified user.
     *                  notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                  unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     *
     */
    public Result fetchActiveUsers(Http.Request request, Long questId){
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Quest requestQuest = questRepository.findById(questId);
        if (requestQuest == null) {
            return notFound(ApiError.notFound());
        }
        List<Profile> activeProfiles = profileRepository.findAllUsing(requestQuest);

        return ok(Json.toJson(activeProfiles));
    }


    /**
     * Fetches all destinations based on Http request query parameters. This also includes pagination, destination
     * ownership and the public or private query.
     *
     * @param request   Http request containing query parameters to filter results.
     * @return          ok() (Http 200) response containing the destinations found in the response body, forbidden()
     *                  (Http 403) if the user has tried to access destinations they are not authorised for.
     */
    private List<Quest> getQuestsQuery(Http.Request request) {

        int pageNumber = 0;
        int pageSize = 50;
        List<Quest> quests;

        ExpressionList<Quest> expressionList = questRepository.getExpressionList();

        if (request.getQueryString(TITLE) != null && !request.getQueryString(TITLE).isEmpty()) {
            expressionList.ilike(TITLE, queryComparator(request.getQueryString(TITLE)));
        }

        if (request.getQueryString(OPERATOR) != null &&
            !request.getQueryString(OPERATOR).isEmpty() &&
            request.getQueryString(OBJECTIVE) != null &&
            !request.getQueryString(OBJECTIVE).isEmpty()) {

            if (request.getQueryString(OPERATOR).equals("=")) {
                expressionList.eq(OBJECTIVE, Double.parseDouble(request.getQueryString(OBJECTIVE)));
            } else if (request.getQueryString(OPERATOR).equals("<")) {
                expressionList.lt(OBJECTIVE, Double.parseDouble(request.getQueryString(OBJECTIVE)));
            } else if (request.getQueryString(OPERATOR).equals(">")) {
                expressionList.gt(OBJECTIVE, Double.parseDouble(request.getQueryString(OBJECTIVE)));
            }
        }

        ExpressionList<Profile> profileExpressionList = profileRepository.getExpressionList();
        Boolean findByOwner = false;

        if (request.getQueryString(FIRSTNAME) != null && !request.getQueryString(FIRSTNAME).isEmpty()) {
            profileExpressionList.ilike(FIRSTNAME, queryComparator(request.getQueryString(FIRSTNAME)));
            findByOwner = true;
        }

        if (request.getQueryString(LASTNAME) != null && !request.getQueryString(LASTNAME).isEmpty()) {
            profileExpressionList.ilike(LASTNAME, queryComparator(request.getQueryString(LASTNAME)));
            findByOwner = true;
        }

        if (findByOwner) {
            List<Profile> profiles = profileExpressionList
                    .select("id")
                    .findList();
            System.out.println(profiles);
            //expressionList.in("owner_id", profiles);
        }

//
//        }
//        if (request.getQueryString(LONGITUDE) != null && !request.getQueryString(LONGITUDE).isEmpty()) {
//            expressionList.eq(LONGITUDE, Double.parseDouble(request.getQueryString(LONGITUDE)));
//        }
//        if (request.getQueryString(DISTRICT) != null && !request.getQueryString(DISTRICT).isEmpty()) {
//            expressionList.ilike(DISTRICT, queryComparator(request.getQueryString(DISTRICT)));
//        }
//        if (request.getQueryString(COUNTRY) != null && !request.getQueryString(COUNTRY).isEmpty()) {
//            expressionList.ilike(COUNTRY, queryComparator(request.getQueryString(COUNTRY)));
//        }
//        if (request.getQueryString(IS_PUBLIC) != null && !request.getQueryString(IS_PUBLIC).isEmpty()) {
//            expressionList.eq(IS_PUBLIC, request.getQueryString(IS_PUBLIC));
//        }

        // If page query is set, load said page. Otherwise, return the first page.
//        if (request.getQueryString(PAGE) != null && !request.getQueryString(PAGE).isEmpty()) {
//            pageNumber = Integer.parseInt(request.getQueryString(PAGE));
//        }

        String startDate = "start_date";

        quests = expressionList
                .order(startDate)
                .findList();

        return quests;
    }
}
