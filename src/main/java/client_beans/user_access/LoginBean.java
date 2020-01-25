package client_beans.user_access;

import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.dtos.MechanicDto;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {

    public static final String HOME_PAGE = "/backoffice/home.xhtml";
    public static final String LOGIN_PAGE = "/user_access/loginPage.xhtml";
    private String message;
    private String userName;
    private String password;
    private MechanicApiController mechanicApiController;
    private AuthenticationApiController authenticationApiController;

    @PostConstruct
    public void init() {
        mechanicApiController = new MechanicApiController();
        authenticationApiController = new AuthenticationApiController();
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

    public String login() throws IOException {

        Optional<MechanicDto> mechanicDto = mechanicApiController.readAll().stream()
                .filter(mechanic -> mechanic.getName().equals(userName) && mechanic.getPassword().equals(password))
                .findFirst();

        if (mechanicDto.isPresent()) {
            Response response = authenticationApiController.authenticateUser(new CredentialsDto(userName, password));
            // get Http Session and store username
            Faces.getSession().setAttribute("username", userName);
            Faces.getSession().setAttribute("mechanic", mechanicDto.get());
            Faces.getSession().setAttribute("token", "Bearer " + response.getEntity());
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath().concat("/backoffice/home.xhtml"));
            return HOME_PAGE;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));

            return LOGIN_PAGE;
        }
    }

    public String logout() {
        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return LOGIN_PAGE;
    }

}
