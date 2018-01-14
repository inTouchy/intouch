package com.unsocialmedia.intouch.cassandra;

import com.unsocialmedia.intouch.Controllers.Customer;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    @Query("Select * from customer where firstname=?0 ALLOW FILTERING")
    public Customer findByFirstName(String firstName);

    @Query("Select * from customer where lastname=?0 ALLOW FILTERING")
    public List<Customer> findByLastName(String lastName);
}
