package front.gateways;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

public class OperationsGateway extends RestGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "app.api.base.path";
    private final String authToken;
    private final String resource;

    public OperationsGateway(String authToken) {
        super();
        this.authToken = authToken;
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + "/delete";
    }

    public void deleteAll() {
        client.target(resource)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

}
