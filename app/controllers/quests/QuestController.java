package controllers.quests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.ApiError;
import models.Profile;
import models.destinations.Destination;
import models.objectives.Objective;
import models.quests.Quest;
import models.quests.QuestAttempt;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.quests.QuestAttemptRepository;
import repositories.quests.QuestRepository;
import util.AuthenticationUtil;
import util.Views;

import java.io.IOException;
import java.util.*;

import static play.mvc.Results.*;
import static util.QueryUtil.queryComparator;

public class QuestController {

    private static final String QUEST_ATTEMPT_EXISTS = "An attempt already exists for this quest.";

    private static final String START_OWN_QUEST = "You cannot start your own quest.";

    private QuestRepository questRepository;
    private QuestAttemptRepository questAttemptRepository;
    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;

    private static final String TITLE = "title";
    private static final String OPERATOR = "operator";
    private static final String OBJECTIVE = "objectives";
    private static final String FIRST_NAME = "first_name";
    private static final String FIRST_NAME_QUERY = "owner.firstName";
    private static final String LAST_NAME_QUERY = "owner.lastName";
    private static final String LAST_NAME = "last_name";
    private static final String COUNTRY = "country";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OWNER = "owner";
    private static final String ATTEMPTS = "attempts";
    private static final String COUNTRY_OCCURRENCES = "objectives.destination.country";
    private static final String EQUAL_TO = "=";
    private static final String GREATER_THAN = ">";
    private static final String LESS_THAN = "<";

    @Inject
    public QuestController(QuestRepository questRepository,
                           QuestAttemptRepository questAttemptRepository,
                           ProfileRepository profileRepository,
                           DestinationRepository destinationRepository) {
        this.questRepository = questRepository;
        this.questAttemptRepository = questAttemptRepository;
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

        Set<Quest> quests = getQuestsQuery(request, loggedInUser);

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
     * Retrieves all the profiles that have the specified quest currently active
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

        // TODO: Cam
//        ObjectMapper mapper = new ObjectMapper();
//        String result;
//        try {
//            result = mapper
//                    .writerWithView(Views.Public.class)
//                    .writeValueAsString(activeProfiles);
//        } catch (JsonProcessingException e) {
//            return badRequest(ApiError.invalidJson());
//        }
        return ok(Json.toJson(activeProfiles));
    }


    /**
     * Creates a new quest attempt for the given quest and user.
     *
     * @param request       the request containing information to start a new quest attempt.
     * @param questId       the id of the quest to be attempted.
     * @param userId        the id of the user attempting the quest.
     * @return              created() (Http 201) response containing the quest attempt.
     *                      notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                      unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     */
    public Result attempt(Http.Request request, Long questId, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Quest questToAttempt = questRepository.findById(questId);
        Profile attemptedBy = profileRepository.findById(userId);
        if (questToAttempt == null || attemptedBy == null) {
            return notFound(ApiError.notFound());
        }

        if (attemptedBy.equals(questToAttempt.getOwner())) {
            return forbidden(ApiError.forbidden(START_OWN_QUEST));
        }

        QuestAttempt attempt = new QuestAttempt(attemptedBy, questToAttempt);

        // Check the user has not already started a quest attempt for the given quest
        if (questAttemptRepository.exists(attempt)) {
            return badRequest(ApiError.badRequest(QUEST_ATTEMPT_EXISTS));
        }

        questAttemptRepository.save(attempt);

        return created(Json.toJson(attempt));
    }


    /**
     * Retrieves all quest attempts for a requested user. This is allowed by any user, as attempted quests are displayed
     * on a user's profile.
     *
     * @param request       the request containing information to get quest attempts.
     * @param userId        the id of user to retrieve quest attempts for.
     * @return              unauthorized() (Http 401) if the user is not logged in.
     *                      notFound() (Http 404) if the requested user doesn't exist.
     *                      badRequest() (Http 400) response containing an ApiError for an invalid Json body.
     *                      ok() (Http 200) containing matching quests that are attempted by the requested profile.
     */
    public Result getQuestAttemptsByProfile(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(userId);
        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        List<QuestAttempt> questAttempts = questAttemptRepository.findAllUsing(requestedUser);


        return getCorrectView(AuthenticationUtil.validUser(loggedInUser, requestedUser), questAttempts);
    }


    /**
     * Retrieves all quests for a requested user that are complete.
     *
     * @param request       the request containing information to get quests completed.
     * @param userId        the id of user that is requesting their completed quests.
     * @return              unauthorized() (Http 401) if the user is not logged in.
     *                      notFound() (Http 404) if the requested user doesn't exist.
     *                      badRequest() (Http 400) response containing an ApiError for an invalid Json body.
     *                      ok() (Http 200) containing matching quests that are completed by the requested profile.
     */
    public Result getQuestsCompletedByProfile(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(userId);
        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        List<Quest> quests = questRepository.findAllCompleted(requestedUser);


        return getCorrectView(AuthenticationUtil.validUser(loggedInUser, requestedUser), quests);

    }


    /**
     * Fetches all destinations based on Http request query parameters. This also includes pagination, destination
     * ownership and the public or private query.
     *
     * @param request   Http request containing query parameters to filter results.
     * @param profile   The profile of the user logged in.
     * @return          ok() (Http 200) response containing the destinations found in the response body,
     *                  forbidden() (Http 403) if the user has tried to access destinations they are not authorised for.
     */
    private Set<Quest> getQuestsQuery(Http.Request request, Profile profile) {

        Set<Quest> quests;

        ExpressionList<Quest> expressionList = questRepository.getExpressionList();

        /*
        Does not include quest that the profile has created
         */
        expressionList.ne(OWNER, profile);

        /*
        Joins all similar quest titles
         */
        if (request.getQueryString(TITLE) != null && !request.getQueryString(TITLE).isEmpty()) {
            expressionList.ilike(TITLE, queryComparator(request.getQueryString(TITLE)));
        }

        /*
        Joins all similar first name owners of quests
         */
        if (request.getQueryString(FIRST_NAME) != null && !request.getQueryString(FIRST_NAME).isEmpty()) {
            expressionList.ilike(FIRST_NAME_QUERY, queryComparator(request.getQueryString(FIRST_NAME)));
        }

        /*
        Joins all similar first name owners of quests
         */
        if (request.getQueryString(LAST_NAME) != null && !request.getQueryString(LAST_NAME).isEmpty()) {
            expressionList.ilike(LAST_NAME_QUERY, queryComparator(request.getQueryString(LAST_NAME)));
        }

        /*
        Joins all quests with countries in specified country
         */
        if (request.getQueryString(COUNTRY) != null && !request.getQueryString(COUNTRY).isEmpty()) {
            expressionList.in(COUNTRY_OCCURRENCES, request.getQueryString(COUNTRY));
        }

        /*
        Joins all quests within valid start and end dates
         */
        expressionList.lt(START_DATE, new Date());
        expressionList.gt(END_DATE, new Date());

        /*
        Removes all quests that the profile has an attempt for
         */
        ExpressionList<Quest> expressionListActiveQuests = questRepository.getExpressionList();

        expressionListActiveQuests.in(ATTEMPTS, questAttemptRepository.findAllUsing(profile));

        Set<Quest> profilesActiveQuests = expressionListActiveQuests.findSet();

        quests = expressionList.findSet();

        quests.removeAll(profilesActiveQuests);

        /*
        Joins all quest if the amount of objectives is correct to the query search.
         */
        if (QueryingObjectiveAmount(request)) {

            Set<Quest> allQuests = new HashSet<>();

            for (Quest quest: quests) {

                if (QuestHasCorrectObjectiveAmount(request, quest)) {
                    allQuests.add(quest);
                }
            }
            return allQuests;
        } else {
            return quests;
        }
    }


    /**
     * Checks if the user is querying on the amount of objectives.
     *
     * @param request       the request sent from the front end user.
     * @return              true if the user is searching for specific objective count.
     *                      false if the user is not searching got a specific objective count.
     */
    private boolean QueryingObjectiveAmount(Http.Request request) {
        return request.getQueryString(OPERATOR) != null &&
                !request.getQueryString(OPERATOR).isEmpty() &&
                request.getQueryString(OBJECTIVE) != null &&
                !request.getQueryString(OBJECTIVE).isEmpty();
    }


    /**
     * Checks if the specified quest has the correct amount of objectives based on the request query string.
     *
     * @param request       the request sent from the front end user.
     * @param quest         the current quest to check for specific quests objective count.
     * @return              true if the quest passes criteria.
     *                      false if the quest fails the criteria.
     */
    private boolean QuestHasCorrectObjectiveAmount(Http.Request request, Quest quest) {
        int objectiveSize = quest.getObjectives().size();

        return request.getQueryString(OPERATOR).equals(EQUAL_TO) &&
                objectiveSize == Double.parseDouble(request.getQueryString(OBJECTIVE)) ||
                request.getQueryString(OPERATOR).equals(LESS_THAN) &&
                        objectiveSize < Double.parseDouble(request.getQueryString(OBJECTIVE)) ||
                request.getQueryString(OPERATOR).equals(GREATER_THAN) &&
                        objectiveSize > Double.parseDouble(request.getQueryString(OBJECTIVE));
    }


    /**
     * Returns the correct jackson view for a given list of requested data based on the logged in users access.
     *
     * @param validAccess        a boolean representing the logged in users view access to this requested data.
     * @param requestedData      the data the logged in user is requesting to view.
     * @return                   badRequest() (Http 400)  response containing an ApiError for an invalid Json body.
     *                           ok() (Http 200) containing matching data that is requested by the logged in user.
     */
    private Result getCorrectView(boolean validAccess, List requestedData) {
        ObjectMapper mapper = new ObjectMapper();
        String result;
        if (validAccess) {
            try {
                result = mapper
                        .writerWithView(Views.Owner.class)
                        .writeValueAsString(requestedData);
            } catch (JsonProcessingException e) {
                return badRequest(ApiError.invalidJson());
            }
        } else {
            try {
                result = mapper
                        .writerWithView(Views.Public.class)
                        .writeValueAsString(requestedData);
            } catch (JsonProcessingException e) {
                return badRequest(ApiError.invalidJson());
            }
        }

        return ok(result);
    }


    /**
     * Attempt to solve the current objective for a given quest attempt.
     *
     * @param request           request containing session information.
     * @param attemptId         the id of the quest attempt to be guessed.
     * @param destinationId     the id of the destination to be used as a guess.
     * @return                  ok() (Http 200) response containing the result of the guess and the quest attempt.
     *                          notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                          unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     *                          forbidden() (Http 403) response containing an ApiError if the user is forbidden from
     *                          guessing for this given attempt.
     */
    public Result guess(Http.Request request, Long attemptId, Long destinationId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        QuestAttempt questAttempt = questAttemptRepository.findById(attemptId);
        Destination destinationGuess = destinationRepository.findById(destinationId);
        if (questAttempt == null || destinationGuess == null) {
            return notFound(ApiError.notFound());
        }

        Profile attemptedBy = questAttempt.getAttemptedBy();
        if (attemptedBy != null && !AuthenticationUtil.validUser(loggedInUser, attemptedBy)) {
            return forbidden(ApiError.forbidden());
        }

        ObjectNode returnJson = new ObjectMapper().createObjectNode();

        // Attempt to solve the current objective in the quest attempt, serialize the result.
        returnJson.put("guessResult",
                questAttempt.solveCurrent(destinationGuess));

        // Serialize quest attempt regardless of result.
        returnJson.set("attempt", Json.toJson(questAttempt));

        questAttemptRepository.update(questAttempt);

        return ok(returnJson);
    }


    /**
     * Check in to the most recently solved objective for a given quest attempt.
     *
     * @param request           request containing session information.
     * @param attemptId         the id of the quest attempt to be checked in to
     * @return                  ok() (Http 200) response containing the quest attempt if check in was successful.
     *                          notFound() (Http 404) response containing an ApiError for retrieval failure.
     *                          unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     *                          forbidden() (Http 403) response containing an ApiError if the user is forbidden from
     *                          guessing for this given attempt.
     */
    public Result checkIn(Http.Request request, Long attemptId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        QuestAttempt questAttempt = questAttemptRepository.findById(attemptId);
        if (questAttempt == null) {
            return notFound(ApiError.notFound());
        }

        Profile attemptedBy = questAttempt.getAttemptedBy();
        if (attemptedBy != null && !AuthenticationUtil.validUser(loggedInUser, attemptedBy)) {
            return forbidden(ApiError.forbidden());
        }

        if (questAttempt.checkIn()) {
            questAttemptRepository.update(questAttempt);
            return ok(Json.toJson(questAttempt));
        }

        // User cannot check-in for this current attempt as they have
        // not solved the current destination or the quest is complete
        return forbidden(ApiError.forbidden());
    }
}
