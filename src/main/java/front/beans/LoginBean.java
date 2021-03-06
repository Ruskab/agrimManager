package front.beans;

import front.dtos.Credentials;
import front.dtos.Mechanic;
import front.gateways.AuthenticationGateway;
import front.gateways.MechanicGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;

@ManagedBean(name = "loginBean") //NOSONAR
@RequestScoped //NOSONAR
public class LoginBean {

    public static final String HOME_PAGE = "/backoffice/dashboard.xhtml";
    public static final String LOGIN_PAGE = "/user_access/loginPage.xhtml";
    private String message;
    private String userName;
    private String password;
    private AuthenticationGateway authenticateGateway;

    private static final Logger LOGGER = LogManager.getLogger(LoginBean.class);

    @ManagedProperty(value = "#{sessionBean}") //NOSONAR
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        authenticateGateway = new AuthenticationGateway();
    }

    public void login() throws IOException {
        try {
            Credentials credentials = Credentials.builder().username(userName).password(password).build();
            String authToken = "Bearer " + authenticateGateway.authenticate(credentials);
            LOGGER.info("Generated authToken");
            initSession(authToken);
            redirect(HOME_PAGE);
        } catch (NotAuthorizedException e) {
            LOGGER.error("No authorized user: {}", userName);
            sendFrontMessage(null, FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!");
            redirect(LOGIN_PAGE);
        }
    }

    private void initSession(String authToken) {
        List<Mechanic> mechanics = new MechanicGateway(authToken).searchByName(userName);
        Faces.getSession().setAttribute("username", userName);
        Mechanic user = mechanics.get(0);
        user.setPassword(null);
        Faces.getSession().setAttribute("token", authToken);
        sessionBean.setMechanic(user);
    }

    private void redirect(String path) throws IOException {
        LOGGER.info("Redirect to Dashboard");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath().concat(path));
    }

    public String logout() {
        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return LOGIN_PAGE;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
