package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


import play.mvc.*;
import play.libs.Json;

import views.html.*;
import java.util.*;

public class Application extends Controller {
    private static final double ERROR_RATE = 0.2d;

    private static final  String REGISTRATION_IDS = "registration_ids";

    private static final String MULTICAST_ID = "multicast_id";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String RESULTS = "results";
    private static final String CANONOCAIL_IDS = "canonical_ids";
    private static final String MESSAGE_ID = "message_id";
    private static final String ERROR = "error";
    private static final String NOT_REGISTERED = "NotRegistered";


    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result sendGCM() {
        Random random = new Random();

        // Get Registration ID array as JsonNode iterator from request body
        JsonNode json = request().body().asJson();
        Iterator<JsonNode> registrationIDs = json.get(REGISTRATION_IDS).elements();

        // Iterate through array to populate Map with Boolean value which indicates Error status
        // Error is determied by ERROR_RATE %tage
        // Currently NotRegistered is only supported
        int error = 0, success = 0;
        ArrayNode results = Json.newArray();

        HashMap<String, Boolean> registrationIDMap = new HashMap<String, Boolean>();
        while (registrationIDs.hasNext()) {
            registrationIDs.next().asText();
            ObjectNode element = Json.newObject();
            double d = Math.random();
            if (d < ERROR_RATE) {
                element.put(ERROR, NOT_REGISTERED);
                //registrationIDMap.put(registId, Boolean.FALSE);
                error++;
            } else {
                element.put(MESSAGE_ID, "1:"+random.nextInt(9999));
                //registrationIDMap.put(registId, Boolean.TRUE);
                success++;
            }
            results.add(element);
        }

        // Create a response.
        ObjectNode response = Json.newObject();
        response.put(MULTICAST_ID, random.nextInt(10000))
                .put(SUCCESS, success)
                .put(FAILURE, error)
                .put(CANONOCAIL_IDS, 0)
                .put(RESULTS, results);

        return ok(response);
    }
}
