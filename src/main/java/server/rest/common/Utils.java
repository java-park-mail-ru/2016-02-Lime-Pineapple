package server.rest.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raaw on 03-Mar-16.
 */
public class Utils {
    public static final String USER_ID_KEY = "id";
    public static final String EMPTY_JSON = new JsonObject().toString(); //{}


    public static Long getIdFromRequestSession(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        return (Long)currentSession.getAttribute(Utils.USER_ID_KEY);
    }
    public static String createJson(Map<String, Object> jsonObjects) {
        final Gson serializer = new Gson();
        return serializer.toJson(jsonObjects);
    }

}
