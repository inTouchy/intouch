package com.unsocialmedia.intouch.Controllers;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table
public class Customer {

    @PrimaryKey
    private UUID id;

    private String firstName;

    private String lastName;

    public Customer() {
    }

    public Customer(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s, firstName='%s', lastName='%s']", this.id,
                this.firstName, this.lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
