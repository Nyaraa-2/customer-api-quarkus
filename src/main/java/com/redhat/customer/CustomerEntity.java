package com.redhat.customer;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer customerId;

    @NotEmpty(message = "Firstname may not to be empty")
    @Column
    private String firstName;

    @Column
    private String middleName;

    @Column
    @NotEmpty(message = "Lastname may not to be empty")
    private String lastName;

    @Column
    private String suffix;

    @Column
    @Email(message = "Error email")
    private String email;

    @Column
    private String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomerEntity that = (CustomerEntity) o;
        return customerId != null && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
