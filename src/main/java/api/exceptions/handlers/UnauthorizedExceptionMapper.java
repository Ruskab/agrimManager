package api.exceptions.handlers;

import api.exceptions.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}