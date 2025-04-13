package org.shoestore.application.customer;

import org.shoestore.domain.model.customer.CustomerRepository;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public void validateCustomerExist(Long customerId) {

        if (!customerRepository.existsById(customerId)) {

            throw new IllegalArgumentException("Customer not found");
        }
    }
}
