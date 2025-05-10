package org.shoestore.application.support;

import org.shoestore.domain.model.customer.CustomerRepository;

public class CustomerValidator {

    private final CustomerRepository customerRepository;

    public CustomerValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateCustomerExist(Long customerId) {

        if (!customerRepository.existsById(customerId)) {

            throw new IllegalArgumentException("Customer not found");
        }
    }
}
