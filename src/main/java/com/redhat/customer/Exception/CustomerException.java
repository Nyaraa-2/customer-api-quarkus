package com.redhat.customer.Exception;

import javax.ws.rs.core.Response;


public class CustomerException extends RuntimeException {

    public CustomerException(Throwable cause) {
        super(cause);
    }

}
