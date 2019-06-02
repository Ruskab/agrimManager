package client_beans.user_access;

import api.api_controllers.MechanicApiController;
import api.daos.MechanicDao;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {

    private String message;
    private String userName;
    private String password;
    private MechanicApiController mechanicApiController;

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
        boolean authorized = mechanicApiController.readAll().stream().anyMatch(mechanic -> mechanic.getName().equals(userName) && mechanic.getPassword().equals(password));

        if (authorized) {
            // get Http Session and store username
            Faces.getSession().setAttribute("username", userName);

            return "/backoffice/clients.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));

            return "/user_access/loginPage.xhtml";
        }
    }

    public String logout() {
        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/user_access/loginPage.xhtml";
    }

}
