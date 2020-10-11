package front.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FrontMessages {

    private FrontMessages() {

    }

    public static void sendFrontMessage(String messagesReference, FacesMessage.Severity severity, String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(messagesReference, new FacesMessage(severity, summary, details));
    }

}
