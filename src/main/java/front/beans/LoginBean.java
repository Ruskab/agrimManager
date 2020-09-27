package front.beans;

import api.dtos.CredentialsDto;
import api.dtos.MechanicDto;
import api.exceptions.UnauthorizedException;
import front.gateways.AuthenticationGateway;
import front.gateways.MechanicGateway;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {

    public static final String HOME_PAGE = "/backoffice/dashboard.xhtml";
    public static final String LOGIN_PAGE = "/user_access/loginPage.xhtml";
    private String message;
    private String userName;
    private String password;
    private AuthenticationGateway authenticateGateway;

    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        authenticateGateway = new AuthenticationGateway();
    }

    public void login() throws IOException {
        try {
            String authToken = "Bearer " + authenticateGateway.authenticate(CredentialsDto.create(userName, password));
            initSession(authToken);
            redirect(HOME_PAGE);
        } catch (UnauthorizedException e) {
            showMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!");
            redirect(LOGIN_PAGE);
        }
    }

    private void initSession(String authToken) {
        List<MechanicDto> mechanics = new MechanicGateway(authToken).searchByCredentials(userName, password);
        if (mechanics.isEmpty()) {
            throw new UnauthorizedException("mechanic not found with given credentials");
        }
        Faces.getSession().setAttribute("username", userName);
        Faces.getSession().setAttribute("mechanic", mechanics.get(0));
        Faces.getSession().setAttribute("token", authToken);
        sessionBean.setMechanicDto(mechanics.get(0));
    }

    private void redirect(String path) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath().concat(path));
    }

    public String logout() {
        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return LOGIN_PAGE;
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
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
