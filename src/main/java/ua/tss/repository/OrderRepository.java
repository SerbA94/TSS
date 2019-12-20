package ua.tss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
