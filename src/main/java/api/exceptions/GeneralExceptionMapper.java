package api.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + throwable.getMessage()).build();
    }
}