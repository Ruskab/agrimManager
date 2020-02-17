package api.exceptions;

public class NotFoundException extends RuntimeException {

    private static final String DESCRIPTION = "Bad Request Exception (400)";

    public static NotFoundException throwBecauseOf(String details){
        throw new NotFoundException(details);
    }

    private NotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}