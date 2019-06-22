package api.exceptions;

public class FieldAlreadyExistException extends ConflictException {
    private static final String DESCRIPTION = "Field Already Exists";

    public FieldAlreadyExistException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}