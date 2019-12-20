package ua.tss.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ua.tss.model.Order;
import ua.tss.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
	
	private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());

        return this.orderRepository.save(order);
    }

    public void update(Order order) {
        this.orderRepository.save(order);
    }

}
