package ua.tss.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tss.model.OrderItem;
import ua.tss.repository.OrderItemRepository;

@Service
@Transactional
public class OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

    public Optional<OrderItem> findById(Long id) {
        return this.orderItemRepository.findById(id);
    }

    public OrderItem create(OrderItem orderItem) {
        return this.orderItemRepository.save(orderItem);
    }

    public Iterable<OrderItem> findAll() {
        return this.orderItemRepository.findAll();
    }

    public OrderItem save(OrderItem orderItem) {
        return this.orderItemRepository.save(orderItem);
    }

    public void delete(OrderItem orderItem) {
        this.orderItemRepository.delete(orderItem);
    }

}
