package api.api_controllers;

import api.business_controllers.AuthenticationBusinessController;
import api.dtos.CredentialsDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Base64;

@Api(value = AuthenticationApiController.AUTH)
@Path(AuthenticationApiController.AUTH)
public class AuthenticationApiController {

    public static final String AUTH = "/authentication";

    private AuthenticationBusinessController authenticationBusinesssController = new AuthenticationBusinessController();


    @POST
    @ApiOperation(value = "Auth mechanic")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(CredentialsDto credentials) {
        try {
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            validate(username, password);
            // Authenticate the user using the credentials provided
            authenticate(username, password);
            // Issue a token for the user
            String token = issueToken(username, password);
            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void authenticate(String username, String password) throws NotFoundException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        authenticationBusinesssController.authenticateCredentials(username, password);
    }

    private String issueToken(String username, String password) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        return Base64.getEncoder().encodeToString(String.format("%s###%s###%s", username, password, LocalDate.now().toString()).getBytes());
    }

    private void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new FieldInvalidException(message + " is missing");
        }
    }

}
