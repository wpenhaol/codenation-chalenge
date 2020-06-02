package br.com.codenation.app.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.Getter;

@Getter
public class FeignWrapperException extends HystrixBadRequestException {

    private static final long serialVersionUID = -8629081206197597990L;
    private final Integer status;

    public FeignWrapperException(final Integer status) {
        super("");
        this.status = status;
    }

}
