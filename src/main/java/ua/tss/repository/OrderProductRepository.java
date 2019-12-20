package ua.tss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.OrderProduct;
import ua.tss.model.OrderProductPK;

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
