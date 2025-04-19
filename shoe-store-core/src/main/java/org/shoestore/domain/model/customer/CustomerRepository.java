package org.shoestore.domain.model.customer;

public interface CustomerRepository {

    boolean existsById(Long id);
}
