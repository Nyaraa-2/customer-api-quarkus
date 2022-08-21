package com.redhat.customer;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
@TestHTTPEndpoint(CustomerResource.class)
@Slf4j
public class CustomerResourceTest {

    @Test
    public void getAll() {
        var t = given()
                .when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(2))
                .body("firstName", hasItems("Marina", "Wendy"))
                .body("lastName", hasItems("Vandenbosch", "Vigneron"))
                .contentType(ContentType.JSON);
        assertThat(t).isNotNull();
    }

    @Test
    public void getById() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        Customer got = given()
                .when()
                .get("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("firstName", equalTo(saved.getFirstName()))
                .body("lastName", equalTo(saved.getLastName()))
                .extract().as(Customer.class);
        assertThat(got).isNotNull();
        assertThat(saved).isEqualTo(got);
    }

    @Test
    public void delete() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        given()
                .when()
                .delete("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    public void put() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        saved.setFirstName("Firstname");
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
        assertThat(saved).isNotNull();
    }

    @Test
    public void post() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getLastName()).isNotNull();
        assertThat(saved.getFirstName()).isNotNull();
        assertThat(saved.getEmail()).isNotNull();
        assertThat(saved.getPhone()).isNotNull();
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/{customerId}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteByIdNotFound() {
        given()
                .when()
                .delete("/{customerId}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void postCustomerAlreadyExist() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getLastName()).isNotNull();
        assertThat(saved.getFirstName()).isNotNull();
        assertThat(saved.getEmail()).isNotNull();
        assertThat(saved.getPhone()).isNotNull();
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());

    }

    @Test
    public void postWithEmptyFirstName() {
        Customer customer = createCustomer();
        customer.setFirstName(null);
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void putByIdNotFound() {
        given()
                .when()
                .put("/{customerId}", 987654321)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void putByIdNotEqualIdCustomer() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getLastName()).isNotNull();
        assertThat(saved.getFirstName()).isNotNull();
        assertThat(saved.getEmail()).isNotNull();
        assertThat(saved.getPhone()).isNotNull();
        int idSaved = saved.getCustomerId();
        saved.setCustomerId(987654321);
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{customerId}", idSaved)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void putWithEmptyFirstName() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isNotNull();
        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getLastName()).isNotNull();
        assertThat(saved.getFirstName()).isNotNull();
        assertThat(saved.getEmail()).isNotNull();
        assertThat(saved.getPhone()).isNotNull();
        saved.setFirstName(null);
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }


    /**
     * Create customer for test
     *
     * @return Customer
     */
    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(RandomStringUtils.randomAlphabetic(10));
        customer.setMiddleName(RandomStringUtils.randomAlphabetic(10));
        customer.setLastName(RandomStringUtils.randomAlphabetic(10));
        customer.setEmail(RandomStringUtils.randomAlphabetic(10) + "@marina.dev");
        customer.setPhone(RandomStringUtils.randomNumeric(10));
        return customer;
    }

}