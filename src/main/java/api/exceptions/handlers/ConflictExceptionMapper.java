package api.exceptions.handlers;

import api.exceptions.ConflictException;
import api.exceptions.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Context private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException e) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorMessage(e, request.getRequestURI() + uriInfo.getRequestUri())).build();
    }
}