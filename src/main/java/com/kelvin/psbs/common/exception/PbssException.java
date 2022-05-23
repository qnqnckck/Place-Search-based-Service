package com.kelvin.psbs.common.exception;

public class PbssException extends RuntimeException {
    public PbssException(String message) {
        super(message);
    }
    public PbssException(Throwable cause) {
        super(cause);
    }
}
