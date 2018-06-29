package br.com.aquiles.report.exception;

import br.com.aquiles.core.exception.CoreException;

public class SRException extends CoreException {
    public SRException(Throwable cause) {
        super(cause);
    }
    public SRException(String message) {
        super(message);
    }
}
