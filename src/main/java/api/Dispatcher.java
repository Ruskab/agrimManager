package api;

import api.api_controllers.*;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.*;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import api.exceptions.RequestInvalidException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dispatcher {

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    private static final Logger LOGGER = LogManager.getLogger(Dispatcher.class);
    private static final String REQUEST_ERROR = "request error: ";
    private ClientApiController clientApiController = new ClientApiController();
    private VehicleApiController vehicleApiController = new VehicleApiController();
    private InterventionApiController interventionApiController = new InterventionApiController();
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private RepairingPackApiController repairingPackApiController = new RepairingPackApiController();

    public void submit(HttpRequest request, HttpResponse response) {
        final String ERROR_MESSAGE = "{'error':'%S'}";
        try {
            switch (request.getMethod()) {
                case POST:
                    this.doPost(request, response);
                    break;
                case GET:
                    this.doGet(request, response);
                    break;
                case PUT:
                    this.doPut(request);
                    break;
                case PATCH:
                    this.doPatch(request);
                    break;
                case DELETE:
                    this.doDelete(request);
                    break;
                default: // Unexpected
                    throw new RequestInvalidException("method error: " + request.getMethod());
            }
        } catch (ArgumentNotValidException | RequestInvalidException exception) {
            response.setBody(String.format(ERROR_MESSAGE, exception.getMessage()));
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException exception) {
            response.setBody(String.format(ERROR_MESSAGE, exception.getMessage()));
            response.setStatus(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {  // Unexpected
            LOGGER.error("context" ,exception);
            response.setBody(String.format(ERROR_MESSAGE, exception));
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void doPatch(HttpRequest request) {
        if (request.isEqualsPath(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)) {
            this.repairingPackApiController.updateReparingPack(request.getPath(1), (String) request.getBody());
        } else {
            throw new RequestInvalidException(REQUEST_ERROR + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doGet(HttpRequest request, HttpResponse response) {
        if (request.isEqualsPath(ClientApiController.CLIENTS)) {
            response.setBody(this.clientApiController.readAll());
        } else if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID)) {
            response.setBody(this.clientApiController.read(request.getPath(1)));
        } else if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES)) {
            response.setBody(this.clientApiController.clientVehiclesList(request.getPath(1)));
        } else if (request.isEqualsPath(VehicleApiController.VEHICLES)) {
            response.setBody(this.vehicleApiController.readAll());
        } else if (request.isEqualsPath(VehicleApiController.VEHICLES + VehicleApiController.ID_ID)) {
            response.setBody(this.vehicleApiController.read(request.getPath(1)));
        } else if (request.isEqualsPath(InterventionApiController.INTERVENTIONS)) {
            response.setBody(this.interventionApiController.readAll());
        } else if (request.isEqualsPath(InterventionApiController.INTERVENTIONS + InterventionApiController.ID)) {
            response.setBody(this.interventionApiController.read(request.getPath(1)));
        } else if (request.isEqualsPath(RepairingPackApiController.REPAIRING_PACKS)) {
            response.setBody(this.repairingPackApiController.readAll());
        } else if (request.isEqualsPath(RepairingPackApiController.REPAIRING_PACKS + RepairingPackApiController.ID)) {
            response.setBody(this.repairingPackApiController.read(request.getPath(1)));
        } else {
            throw new NotFoundException(REQUEST_ERROR + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doDelete(HttpRequest request) {
        if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID)) {
            this.clientApiController.delete(request.getPath(1));
        } else if (request.isEqualsPath(VehicleApiController.VEHICLES + VehicleApiController.ID_ID)) {
            this.vehicleApiController.delete(request.getPath(1));
        } else if (request.isEqualsPath(InterventionApiController.INTERVENTIONS + InterventionApiController.ID)) {
            this.interventionApiController.delete(request.getPath(1));
        } else {
            throw new NotFoundException(REQUEST_ERROR + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doPut(HttpRequest request) {
        if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID)) {
            this.clientApiController.update(request.getPath(1), (ClientDto) request.getBody());
        } else {
            throw new NotFoundException(REQUEST_ERROR + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doPost(HttpRequest request, HttpResponse response) {
        if (request.isEqualsPath(ClientApiController.CLIENTS)) {
            response.setBody(this.clientApiController.create((ClientDto) request.getBody()));
        } else if (request.isEqualsPath(VehicleApiController.VEHICLES)) {
            response.setBody(this.vehicleApiController.create((VehicleDto) request.getBody()));
        } else if (request.isEqualsPath(InterventionApiController.INTERVENTIONS)) {
            response.setBody(this.interventionApiController.create((InterventionDto) request.getBody()));
        } else if (request.isEqualsPath(MechanicApiController.MECHANICS)) {
            response.setBody(this.mechanicApiController.create((MechanicDto) request.getBody()));
        } else if (request.isEqualsPath(RepairingPackApiController.REPAIRING_PACKS)) {
            response.setBody(this.repairingPackApiController.create((RepairingPackDto) request.getBody()));
        } else if (request.isEqualsPath(MechanicApiController.MECHANICS + MechanicApiController.ID_INTERVENTIONS)) {
            this.mechanicApiController.createIntervention(request.getPath(1), (InterventionDto) request.getBody());
        } else {
            throw new RequestInvalidException(REQUEST_ERROR + request.getMethod() + ' ' + request.getPath());
        }
    }
}
