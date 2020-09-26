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
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {

    public static final String HOME_PAGE = "/backoffice/dashboard.xhtml";
    public static final String LOGIN_PAGE = "/user_access/loginPage.xhtml";
    private String message;
    private String userName;
    private String password;
    private AuthenticationGateway authenticateGateway;

    @PostConstruct
    public void init() {
        authenticateGateway = new AuthenticationGateway();
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

    public void login() throws IOException {
        try {
            String authToken = "Bearer " + authenticateGateway.authenticate(CredentialsDto.create(userName, password));
            MechanicDto mechanicDto = new MechanicGateway(authToken).readAll().stream()
                    .filter(mechanic -> mechanic.getName().equals(userName) && mechanic.getPassword().equals(password))
                    .findFirst().orElseThrow(() -> new UnauthorizedException("mechanic not found with given credentials"));

            Faces.getSession().setAttribute("username", userName);
            Faces.getSession().setAttribute("mechanic", mechanicDto);
            Faces.getSession().setAttribute("token", authToken);
            redirect(HOME_PAGE);

        } catch (UnauthorizedException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));
            redirect(LOGIN_PAGE);
        }
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

}
