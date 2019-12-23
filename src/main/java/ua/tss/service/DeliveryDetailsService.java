package ua.tss.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.tss.model.DeliveryDetails;
import ua.tss.repository.DeliveryDetailsRepository;

@Service
@Transactional
public class DeliveryDetailsService {
	
	private DeliveryDetailsRepository deliveryDetailsRepository;

    public DeliveryDetailsService(DeliveryDetailsRepository deliveryDetailsRepository) {
        this.deliveryDetailsRepository = deliveryDetailsRepository;
    }

    public DeliveryDetails create(DeliveryDetails deliveryDetails) {
        return this.deliveryDetailsRepository.save(deliveryDetails);
    }

    public void update(DeliveryDetails deliveryDetails) {
        this.deliveryDetailsRepository.save(deliveryDetails);
    }
}
