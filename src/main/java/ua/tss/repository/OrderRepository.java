package ua.tss.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

	@Query(
			value = "SELECT * FROM orders WHERE delivery_details_id IN ("
					+ "SELECT id FROM delivery_details WHERE user_user_id = ?1);",
			nativeQuery = true)
	public Iterable<Order> getAllUserOrders(Long id);

}
