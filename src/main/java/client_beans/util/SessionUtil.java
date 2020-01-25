package client_beans.util;

import api.dtos.MechanicDto;
import org.omnifaces.util.Faces;

public class SessionUtil {

    public static String getAuthToken() {
        return (String) Faces.getSession().getAttribute("token");
    }

    public static MechanicDto getSessionMechanic(String key) {
        return (MechanicDto) Faces.getSession().getAttribute(key);
    }
}
