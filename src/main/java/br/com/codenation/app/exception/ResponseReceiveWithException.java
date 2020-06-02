package br.com.codenation.app.exception;

import java.io.IOException;

public class ResponseReceiveWithException extends RuntimeException {

    private static final long serialVersionUID = -6339899353764397631L;
    public ResponseReceiveWithException(IOException e) {
        super(e);
    }
}
