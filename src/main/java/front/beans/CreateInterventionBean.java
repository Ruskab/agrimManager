package front.beans;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import api.entity.InterventionType;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class CreateInterventionBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateInterventionBean.class);
    private VehicleDto selectedVehicle;
    private InterventionDto selectedIntervention;
    private MechanicGateway mechanicGateway;
    private VehicleGateway vehicleGateway;
    private boolean isCaffe;

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        selectedIntervention = new InterventionDto();
    }

    public void create() throws IOException {
        MechanicDto mechanic = (MechanicDto) Faces.getSession().getAttribute("mechanic");
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

    public List<VehicleDto> searchVehicles(String query) {
        return vehicleGateway.searchBy(query);
    }

    private void validateSelection() {
        if (selectedIntervention == null) {
            LOGGER.error("Intervention empty");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Intervention empty", ""));
        }
    }

    private void resetWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('selectVehicleTab', false)");
        selectedIntervention = new InterventionDto();
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

    public VehicleDto getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(VehicleDto selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public InterventionDto getSelectedIntervention() {
        return selectedIntervention;
    }

    public void setSelectedIntervention(InterventionDto selectedIntervention) {
        this.selectedIntervention = selectedIntervention;
    }
}
