package org.shoestore.domain.model.product;

import java.util.List;
import java.util.Set;

public interface ProductRepository {

    Product findById(Long id);

    List<Product> findAllByIds(Set<Long> ids);
}
