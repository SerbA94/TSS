package ua.tss.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tss.model.OrderProduct;
import ua.tss.repository.OrderProductRepository;

@Service
@Transactional
public class OrderProductService {

    @Autowired
	private OrderProductRepository orderProductRepository;

    public OrderProduct create(OrderProduct orderProduct) {
        return this.orderProductRepository.save(orderProduct);
    }

}
