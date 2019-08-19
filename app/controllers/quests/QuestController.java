package controllers.quests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.ApiError;
import models.Profile;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static play.mvc.Results.*;

public class QuestController {

    private static final String QUEST_ATTEMPT_EXISTS = "An attempt already exists for this quest.";

    private QuestRepository questRepository;
    private QuestAttemptRepository questAttemptRepository;
    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;

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

        List<Quest> questQuery = questRepository.findAll();
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
     * @param request       the request containing information to start a new quest attempt.
     * @param userId        the id of user that is having their quest attempts requested.
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

        ObjectMapper mapper = new ObjectMapper();
        String result;

        if (AuthenticationUtil.validUser(loggedInUser, requestedUser)) {
            try {
                result = mapper
                        .writerWithView(Views.Owner.class)
                        .writeValueAsString(questAttempts);
            } catch (JsonProcessingException e) {
                return badRequest(ApiError.invalidJson());
            }
        } else {
            try {
                result = mapper
                        .writerWithView(Views.Public.class)
                        .writeValueAsString(questAttempts);
            } catch (JsonProcessingException e) {
                return badRequest(ApiError.invalidJson());
            }
        }

        return ok(result);
    }
}
