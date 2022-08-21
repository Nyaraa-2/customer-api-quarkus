package com.redhat.customer.Exception;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExceptionMapper {
    @ServerExceptionMapper
    public RestResponse<Object> mapException(CustomerException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("FileName", e.getStackTrace()[0].getFileName());
        errors.put("Error", e.getCause().getMessage());
        if (e.getCause() instanceof NotFoundException)
            return RestResponse.status(Response.Status.NOT_FOUND, errors);
        return RestResponse.status(Response.Status.BAD_REQUEST, errors);
    }
}
