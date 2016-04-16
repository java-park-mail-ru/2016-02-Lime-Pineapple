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
public abstract class Utils {
    public final static String USER_ID_KEY = "id";
    public final static String EMPTY_JSON = new JsonObject().toString(); //{}


    public static Long GetIdFromRequestSession(HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        return (Long)currentSession.getAttribute(Utils.USER_ID_KEY);
    }
    public static String CreateJson(Map<String, Object> jsonObjects) {
        Gson serializer = new Gson();
        return serializer.toJson(jsonObjects);
    }

}
