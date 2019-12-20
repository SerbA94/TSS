package ua.tss.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ua.tss.model.OrderProduct;
import ua.tss.repository.OrderProductRepository;

@Service
@Transactional
public class OrderProductService {
	
	private OrderProductRepository orderProductRepository;

    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public OrderProduct create(OrderProduct orderProduct) {
        return this.orderProductRepository.save(orderProduct);
    }

}
