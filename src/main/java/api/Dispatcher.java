package api;

import api.apiControllers.ClientApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import api.exceptions.RequestInvalidException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

public class Dispatcher {

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    private ClientApiController clientApiController = new ClientApiController();
    private VehicleApiController vehicleApiController = new VehicleApiController();

    public void submit(HttpRequest request, HttpResponse response) {
        String ERROR_MESSAGE = "{'error':'%S'}";
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
                    //this.doPatch(request);
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
            exception.printStackTrace();
            response.setBody(String.format(ERROR_MESSAGE, exception));
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void doGet(HttpRequest request, HttpResponse response) {
        if (request.isEqualsPath(ClientApiController.CLIENTS)){
            response.setBody(this.clientApiController.readAll());
        }else if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID_ID)){
            response.setBody(this.clientApiController.read(request.getPath(1)));
        }
    }

    private void doDelete(HttpRequest request) {
        if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID_ID)){
            this.clientApiController.delete(request.getPath(1));
        }else{
            throw new NotFoundException("request error: " + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doPut(HttpRequest request) {
        if (request.isEqualsPath(ClientApiController.CLIENTS + ClientApiController.ID_ID)) {
            this.clientApiController.update(request.getPath(1), (ClientDto) request.getBody());
        } else {
            throw new NotFoundException("request error: " + request.getMethod() + ' ' + request.getPath());
        }
    }

    private void doPost(HttpRequest request, HttpResponse response) {
        if (request.isEqualsPath(ClientApiController.CLIENTS)) {
            response.setBody(this.clientApiController.create((ClientDto) request.getBody()));
        }else if(request.isEqualsPath(VehicleApiController.VEHICLES)){
            response.setBody(this.vehicleApiController.create((VehicleDto) request.getBody()));
        } else {
            throw new RequestInvalidException("request error: " + request.getMethod() + ' ' + request.getPath());
        }
    }
}
