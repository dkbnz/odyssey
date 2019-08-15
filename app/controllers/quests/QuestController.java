package controllers.quests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.protobuf.Api;
import models.ApiError;
import models.Profile;
import models.quests.Quest;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.quests.QuestRepository;
import util.AuthenticationUtil;
import util.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.*;

public class QuestController {

    private QuestRepository questRepository;
    private ProfileRepository profileRepository;

    @Inject
    public QuestController(QuestRepository questRepository,
                           ProfileRepository profileRepository) {
        this.questRepository = questRepository;
        this.profileRepository = profileRepository;

    }


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

        List<ApiError> questCreationErrors = new ArrayList<>();

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

        questCreationErrors.addAll(newQuest.getErrors());

        if (!questCreationErrors.isEmpty()) {
            return badRequest(Json.toJson(questCreationErrors));
        }

        questRepository.save(newQuest);
        profileRepository.update(questOwner);

        return created(Json.toJson(newQuest));
    }



}
