package api.exceptions;

public class BadRequestException extends RuntimeException {
    private static final String DESCRIPTION = "Bad Request Exception (400)";

    public static BadRequestException throwBecauseOf(String details){
        throw new BadRequestException(details);
    }

    public BadRequestException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}