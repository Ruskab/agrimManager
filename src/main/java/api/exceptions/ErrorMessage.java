package api.exceptions;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

    private String error;
    private String message;
    private String path;

    public ErrorMessage(){

    }

    public ErrorMessage(Exception exception, String path) {
        this.error = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ErrorMessage [error=" + error + ", message=" + message + ", path=" + path + "]";
    }
}