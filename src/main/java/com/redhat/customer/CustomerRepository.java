package com.redhat.customer;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity,Integer> {
    public Optional<CustomerEntity> findByFirstNameAndLastName(String firstName, String lastName) {
        return find("firstName",firstName).stream().findAny();
    }
}
