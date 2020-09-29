package front.util;

import front.dtos.Mechanic;
import org.omnifaces.util.Faces;

public class SessionUtil {

    private SessionUtil(){}

    public static String getAuthToken() {
        return (String) Faces.getSession().getAttribute("token");
    }

    public static Mechanic getSessionMechanic(String key) {
        return (Mechanic) Faces.getSession().getAttribute(key);
    }
}
