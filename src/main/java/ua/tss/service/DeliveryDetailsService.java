package ua.tss.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tss.model.DeliveryDetails;
import ua.tss.repository.DeliveryDetailsRepository;

@Service
@Transactional
public class DeliveryDetailsService {

    @Autowired
	private DeliveryDetailsRepository deliveryDetailsRepository;

    public DeliveryDetails create(DeliveryDetails deliveryDetails) {
        return this.deliveryDetailsRepository.save(deliveryDetails);
    }

    public void update(DeliveryDetails deliveryDetails) {
        this.deliveryDetailsRepository.save(deliveryDetails);
    }

    public Optional<DeliveryDetails> getLastDeliveryDetails() {
        return this.deliveryDetailsRepository.getLastDeliveryDetails();
    }
}
