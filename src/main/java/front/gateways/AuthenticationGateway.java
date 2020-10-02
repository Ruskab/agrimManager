package front.gateways;

import front.dtos.Credentials;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

public class AuthenticationGateway extends RestGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String AUTHENTICATION = "api.authentication.path";
    private static final String API_PATH = "app.api.base.path";
    private final String resource;

    public AuthenticationGateway() {
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(AUTHENTICATION);
    }

    public String authenticate(Credentials credentials) throws NotAuthorizedException {
        Response response = client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
        return response.readEntity(String.class);
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new NotAuthorizedException(String.format("API response invalid status: %s", response.getStatus()));
        }
    }

}
