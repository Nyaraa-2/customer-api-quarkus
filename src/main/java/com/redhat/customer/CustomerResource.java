package com.redhat.customer;

import com.redhat.customer.Exception.CustomerException;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "customer", description = "Customer Operations")
@Slf4j
public class CustomerResource {
    @Inject
    CustomerService customerService;


    /**
     * GET all customers
     * @return Response
     */
    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Customers",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "No results in database",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON
            )
    )
    public Response get() {
        return Response.ok(customerService.findAll()).build();
    }

    /**
     * GET customer by id
     * @param customerId CustomerId
     * @return Response
     */
    @GET
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "200",
            description = "Get Customer by customer id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Customer does not exist",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON
            )
    )
    public Response getById(@Parameter(name = "customerId", required = true) @PathParam("customerId") Integer customerId) {
        return customerService.findById(customerId)
                .map(customer -> Response.ok(customer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * POST customer
     * @param customer domain
     * @param uriInfo context
     * @return Response
     */
    @POST
    @APIResponse(
            responseCode = "201",
            description = "Customer Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Customer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Customer already exists for customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid Customer customer, @Context UriInfo uriInfo) {
        customerService.save(customer);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(customer.getCustomerId())).build();
        return Response.created(uri).entity(customer).build();
    }

    /**
     * PUT customer
     * @param customerId customer id
     * @param customer Customer
     * @return Response
     */
    @PUT
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "202",
            description = "Customer updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Customer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Customer object does not have customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable customerId does not match Customer.customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Customer found for customerId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "customerId", required = true) @PathParam("customerId") Integer customerId, @NotNull @Valid Customer customer,@Context UriInfo uriInfo){
        Customer customerUpdated = customerService.update(customer, customerId);
        return Response.accepted(customerUpdated).build();
    }

    /**
     * DELETE customer
     * @param customerId id customer
     * @return Response 202
     */
    @DELETE
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "202",
            description = "Delete Customer by customer id",
            content = @Content(
                    mediaType = MediaType.TEXT_PLAIN
            )
    )
    public Response delete(@Parameter(name = "customerId", required = true) @PathParam("customerId") Integer customerId){
        customerService.delete(customerId);
        return Response.status(Response.Status.ACCEPTED.getStatusCode(),"Delete account ok").build();
    }
}