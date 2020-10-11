package front.beans;

import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
public class CreateInterventionBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateInterventionBean.class);
    private Vehicle selectedVehicle;
    private Intervention selectedIntervention;
    private MechanicGateway mechanicGateway;
    private VehicleGateway vehicleGateway;
    private boolean isCaffe;

    @ManagedProperty(value = "#{sessionBean}") //NOSONAR
    private SessionBean sessionBean;


    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        selectedIntervention = new Intervention();
    }

    public void create() throws IOException {
        Mechanic mechanic = sessionBean.getMechanic();
        validateSelection();
        selectedIntervention.setStartTime(LocalDateTime.now());
        selectedIntervention.setVehicleId(selectedVehicle != null ? Integer.toString(selectedVehicle.getId()) : null);
        selectedIntervention.setInterventionType(selectedVehicle != null ? "REPAIR" : "CAFFE");
        mechanicGateway.createIntervention(mechanic, selectedIntervention);
        PrimeFaces.current().executeScript("PF('createIntervention').hide();");
        resetWizard();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath().concat("/backoffice/dashboard.xhtml"));
    }

    public List<Vehicle> searchVehicles(String query) {
        return vehicleGateway.searchBy(query);
    }

    private void validateSelection() {
        if (selectedIntervention == null) {
            LOGGER.error("Intervention empty");
            sendFrontMessage("confirmMessages", SEVERITY_ERROR, "Intervention empty", "");
        }
    }

    private void resetWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('selectVehicleTab', false)");
        selectedIntervention = new Intervention();
        selectedVehicle = null;
    }

    public boolean isCaffe() {
        return isCaffe;
    }

    public void setCaffe(boolean caffe) {
        this.isCaffe = caffe;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public Intervention getSelectedIntervention() {
        return selectedIntervention;
    }

    public void setSelectedIntervention(Intervention selectedIntervention) {
        this.selectedIntervention = selectedIntervention;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
