package client_beans.user_access;

import api.api_controllers.MechanicApiController;
import api.dtos.MechanicDto;
import api.entity.Role;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {

    private String message;
    private String userName;
    private String password;
    private MechanicApiController mechanicApiController;
    public static final String CLIENT_PAGE = "/backoffice/clients.xhtml";
    public static final String LOGIN_PAGE = "/user_access/loginPage.xhtml";

    @PostConstruct
    public void init() {
        mechanicApiController = new MechanicApiController();
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

    public String login() {

        Optional<MechanicDto> mechanicDto = mechanicApiController.readAll().stream()
                .filter(mechanic -> mechanic.getName().equals(userName) && mechanic.getPassword().equals(password))
                .findFirst();

        if (mechanicDto.isPresent()) {
            // get Http Session and store username
            Faces.getSession().setAttribute("username", userName);
            Faces.getSession().setAttribute("mechanic", mechanicDto.get());
            return CLIENT_PAGE;
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
