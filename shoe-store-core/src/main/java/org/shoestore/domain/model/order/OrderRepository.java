package org.shoestore.domain.model.order;

public interface OrderRepository {

    Order save(Order order);

    Order update(Order order);

    Order findById(Long id);
}
