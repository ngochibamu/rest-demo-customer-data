package org.retail.customers.dao;

import org.retail.customers.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dominic Chibamu
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
