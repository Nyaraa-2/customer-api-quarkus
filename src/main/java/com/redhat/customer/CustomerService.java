package com.redhat.customer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.redhat.customer.Exception.CustomerException;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private static final String NO_RESULTS = "There are no results for this search";
    private static final String DELETE_ERROR_CUSTOMER_NOT_FOUND = "This customer account does not exist";
    private static final String CUSTOMER_ALREADY_EXIST = "Customer already exist";
    private static final String DELETE_ERROR = "Customer not deleted";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Find all customers
     *
     * @return List of Customer
     * @throws CustomerException NotFoundException if no results
     */
    public List<Customer> findAll() {
        List<Customer> customers = customerRepository.listAll().stream().map(customerMapper::toDomain).collect(Collectors.toList());
        if (customers.size() == 0)
            throw new CustomerException(new NotFoundException(NO_RESULTS));
        return customers;
    }

    /**
     * Find customer by id
     *
     * @param customerId ID Customer
     * @return Optional<Customer>
     * @throws CustomerException NotFoundException if no results
     */
    public Optional<Customer> findById(@NonNull Integer customerId) {
        return Optional.ofNullable(customerRepository.findByIdOptional(customerId)
                .map(customerMapper::toDomain)
                .orElseThrow(() -> new CustomerException(new NotFoundException(DELETE_ERROR_CUSTOMER_NOT_FOUND))));
    }

    /**
     * Create customer, data persist on save in resource
     *
     * @param customer Customer domain
     */
    @Transactional
    public void save(@Valid Customer customer) {
        log.debug("Saving Customer: {}", customer);
        if (customerRepository.findByFirstNameAndLastName(customer.getFirstName(), customer.getLastName()).isPresent())
            throw new CustomerException(new BadRequestException(CUSTOMER_ALREADY_EXIST));
        CustomerEntity entity = customerMapper.toEntity(customer);
        customerRepository.persist(entity);
        customerMapper.updateDomainFromEntity(entity, customer);
    }

    /**
     * Update customer data persist on save in resource
     *
     * @param customer   Customer
     * @param customerId CustomerId
     * @return Customer update
     */
    @Transactional
    public Customer update(@Valid Customer customer, @NonNull Integer customerId) {
        log.debug("Customer updated: {}", customer);
        if (Objects.isNull(customer.getCustomerId()))
            throw new CustomerException(new BadRequestException("Customer does not have a customerId"));
        if (!Objects.equals(customerId, customer.getCustomerId()))
            throw new CustomerException(new BadRequestException("Path variable customerId does not match Customer.customerId"));
        CustomerEntity entity = customerRepository.findByIdOptional(customer.getCustomerId())
                .orElseThrow(() -> new CustomerException(new NotFoundException("No Customer found for customerId")));
        customerMapper.updateEntityFromDomain(customer, entity);
        customerRepository.persist(entity);
        customerMapper.updateDomainFromEntity(entity, customer);
        return customer;
    }

    /**
     * Delete customer account
     *
     * @param customerId ID Customer
     */
    @Transactional
    public void delete(@NonNull Integer customerId) {
        Optional<CustomerEntity> entity = Optional.ofNullable(customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerException(new NotFoundException(DELETE_ERROR_CUSTOMER_NOT_FOUND))));
        boolean deleted = customerRepository.deleteById(customerId);
        if (!deleted)
            throw new CustomerException(new BadRequestException(DELETE_ERROR));
    }
}
