package api.exceptions.handlers;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<javax.ws.rs.ClientErrorException> {

    @Override
    public Response toResponse(ClientErrorException e) {
        return e.getResponse();
    }
}