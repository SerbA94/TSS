package ua.tss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.DeliveryDetails;

@Repository
public interface DeliveryDetailsRepository extends CrudRepository<DeliveryDetails, Long> {

}
