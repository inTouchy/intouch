package com.unsocialmedia.intouch.Controllers;

import com.datastax.driver.core.utils.UUIDs;
import com.unsocialmedia.intouch.cassandra.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mohed on 2017-04-26.
 */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    @Autowired
    private CustomerRepository repository;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/cassandra")
    public Customer cassandraTest() {


        this.repository.deleteAll();

        // save a couple of customers
        this.repository.save(new Customer(UUIDs.timeBased(), "Alice", "Smith"));
        this.repository.save(new Customer(UUIDs.timeBased(), "Bob", "Smith"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : this.repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        Customer returnCustomer = null;
        for (Customer customer : this.repository.findByLastName("Smith")) {
            System.out.println(customer);
            if ("Bob".equals(customer.getFirstName())) {
                returnCustomer = customer;
            }
        }
        return returnCustomer;
    }
}