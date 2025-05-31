package org.shoestore.domain.model.product;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductRepository {

    Product findById(Long id);

    List<Product> findAllByIds(Set<Long> ids);

    Map<Long, Product> findAllByIdsAsMap(Set<Long> ids);

    List<Product> saveAll(List<Product> products);
}
