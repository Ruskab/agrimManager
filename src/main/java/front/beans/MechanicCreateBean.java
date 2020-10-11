package front.beans;

import com.mysql.cj.util.StringUtils;
import front.dtos.Mechanic;
import front.gateways.MechanicGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
public class MechanicCreateBean {

    private static final Logger LOGGER = LogManager.getLogger(MechanicCreateBean.class);
    private MechanicGateway mechanicGateway;
    private Mechanic mechanic;
    private List<String> selectedRoles = new ArrayList<>();

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        mechanic = new Mechanic();
    }

    public void create() {
        if (mechanic == null) {
            LOGGER.error("Mecanico vacio");
            sendFrontMessage("confirmMessages", SEVERITY_ERROR, "Client empty", "");
        }
        String mechanicId = mechanicGateway.create(mechanic);
        String message = StringUtils.isStrictlyNumeric(mechanicId) ? "Successful" : "Error";
        if ("Error".equals(message)) {
            sendFrontMessage("confirmMessages", SEVERITY_ERROR, message, "create vehicle");
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

    public List<String> getRoles() {
        return List.of("ADMIN", "DEVELOPER", "MECHANIC");
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
