package ua.tss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.DeliveryDetails;

@Repository
public interface DeliveryDetailsRepository extends CrudRepository<DeliveryDetails, Long> {

	@Query(
		value = "SELECT * FROM delivery_details WHERE user_user_id IN (SELECT max(date_created) FROM orders);",
		nativeQuery = true)
    public Optional<DeliveryDetails> getLastDeliveryDetails();


}
