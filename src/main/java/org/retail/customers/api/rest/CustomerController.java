package org.retail.customers.api.rest;

import org.retail.customers.domain.Customer;
import org.retail.customers.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Dominic Chibamu
 */
@RestController
@RequestMapping(value = "/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Customer newCustomer = customerService.createCustomer(customer);
        response.setHeader("Location", request.getRequestURL().append("/").append(newCustomer.getId()).toString());
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET,produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Customer getCustomer(@PathVariable("id") Long customerId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        return customerService.getCustomer(customerId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,   produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long customerId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        customerService.deleteCustomer(customerId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@PathVariable("id") Long customerId, @RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response){
        if(customerId != customer.getId()){
            throw new RuntimeException("Attempt to update wrong customer");
        }
        customerService.updateCustomer(customer);
    }
}
