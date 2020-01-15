package ua.tss.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tss.model.Order;
import ua.tss.model.User;
import ua.tss.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
	private OrderRepository orderRepository;

    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Iterable<Order> getAllUserOrders(User user) {
        return this.orderRepository.getAllUserOrders(user.getId());
    }

    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());
        order.setDateUpdated(LocalDate.now());
        return this.orderRepository.save(order);
    }

    public void update(Order order) {
        order.setDateUpdated(LocalDate.now());
        this.orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return this.orderRepository.findById(id);
    }

    public void delete(Order order) {
        this.orderRepository.delete(order);
    }

}
