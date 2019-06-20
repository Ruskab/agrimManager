package api.exceptions;

public class NotFoundException extends RuntimeException {

    private static final String DESCRIPTION = "Bad Request Exception (400)";

    public NotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}