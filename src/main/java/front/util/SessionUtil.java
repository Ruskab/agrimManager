package front.util;

import org.omnifaces.util.Faces;

public class SessionUtil {

    private SessionUtil() {
    }

    public static String getAuthToken() {
        return (String) Faces.getSession().getAttribute("token");
    }

}
