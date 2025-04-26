package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.customer.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @BeforeAll
    public static void beforeTest() {


    }

    @Test
    @DisplayName("Customer Existence Test")
    public void testValidateCustomerExist() {

        // given
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.existsById(2L)).thenReturn(false);

        // then
        assertDoesNotThrow(() -> customerService.validateCustomerExist(1L));
        assertThrows(IllegalArgumentException.class, () -> customerService.validateCustomerExist(2L));
    }
}
