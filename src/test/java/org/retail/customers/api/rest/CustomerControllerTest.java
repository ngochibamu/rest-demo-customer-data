package org.retail.customers.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.retail.customers.Application;
import org.retail.customers.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.retail.customers.api.rest.TestingConstants.REQUEST_MAPPING_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class CustomerControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(Customer.class);

    @InjectMocks
    CustomerController customerController;

    @Autowired
    WebApplicationContext webAppCtx;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppCtx).build();
    }

    @Test
    public void shouldSaveFindAndDeleteResourceTest() throws Exception{

        Customer customer = createCustomerObject();
        byte[] customerJson = objectToJson(customer);

        MvcResult mvcResult = mockMvc.perform(post(REQUEST_MAPPING_PATH)
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        long customerId = extractCustomerId(mvcResult.getResponse().getRedirectedUrl());

        mockMvc.perform(get(REQUEST_MAPPING_PATH+"/" + customerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) customerId)))
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(customer.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(customer.getPhoneNumber())))
                .andExpect(jsonPath("$.emailAddress", is(customer.getEmailAddress())));

        mockMvc.perform(delete(REQUEST_MAPPING_PATH+"/"+customerId)).andExpect(status().isNoContent());

    }

    private Customer createCustomerObject(){
        Customer customer = new Customer();
        customer.setFirstName(TestingConstants.FIRST_NAME);
        customer.setLastName(TestingConstants.LAST_NAME);
        customer.setPhoneNumber(TestingConstants.PHONE_NUMBER);
        customer.setEmailAddress(TestingConstants.EMAIL_ADDRESS);
        return customer;
    }

    private byte[] objectToJson(Customer customer) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(customer).getBytes();
    }

    private Long extractCustomerId(String responseUrl){
        String elements[] = responseUrl.split("/");
        return Long.valueOf(elements[elements.length - 1]);
    }
}
