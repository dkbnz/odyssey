package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.TravellerType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.List;

public class TravellerTypeController extends Controller {

    public Result list() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();
        ObjectNode travTypeobj;

        List<TravellerType> travTypes = TravellerType.find.all();


        for(TravellerType travtype : travTypes){
            travTypeobj = (ObjectNode) Json.toJson(travtype);
            travTypeobj.remove("profiles");
            results.add(travTypeobj);
        }

        return ok(results);
    }

}