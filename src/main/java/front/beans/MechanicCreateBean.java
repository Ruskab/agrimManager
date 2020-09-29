package front.beans;

import front.dtos.Mechanic;
import api.entity.Role;
import com.mysql.cj.util.StringUtils;
import front.gateways.MechanicGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class MechanicCreateBean {

    private static final Logger LOGGER = LogManager.getLogger(MechanicCreateBean.class);
    private MechanicGateway mechanicGateway;
    private Mechanic mechanic;
    private List<Role> selectedRoles = new ArrayList<>();

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        mechanic = new Mechanic();
    }

    public void create() {
        if (mechanic == null) {
            LOGGER.error("Mecanico vacio");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }
        String mechanicId = mechanicGateway.create(mechanic);
        String message = StringUtils.isStrictlyNumeric(mechanicId) ? "Successful" : "Error";
        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "create vehicle"));
            return;
        }
        PrimeFaces.current().executeScript("PF('vehicleCreateDialog').hide();");
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Role[] getRoles() {
        return Role.values();
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
