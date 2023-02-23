package org.cube.commons.exception;

public class CubeAppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CubeAppException(String message) {
        super(message);
    }

    public CubeAppException(Throwable cause) {
        super(cause);
    }

    public CubeAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
