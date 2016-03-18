package server.rest.common;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Raaw on 03-Mar-16.
 */
public abstract class Utils {
    public static final String USER_ID_KEY = "id";

    public static Long GetIdFromRequestSession(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        return (Long)currentSession.getAttribute(Utils.USER_ID_KEY);
    }
    public static String CreateJson(Map<String, Object> jsonObjects) {
        final Gson serializer = new Gson();
        return serializer.toJson(jsonObjects);
    }

}
