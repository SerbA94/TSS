package ua.tss.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.tss.model.Order;
import ua.tss.model.OrderProduct;
import ua.tss.model.Product;
import ua.tss.model.User;
import ua.tss.model.dto.OrderProductDto;
import ua.tss.model.enums.DeliveryStatus;
import ua.tss.model.enums.PaymentStatus;
import ua.tss.repository.OrderRepository;
import ua.tss.repository.ProductRepository;
import ua.tss.repository.UserRepository;
import ua.tss.service.OrderProductService;
import ua.tss.service.OrderService;

@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String create(Model model, @RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray) {

		if (nameArray.length != quantityArray.length) {
			return "error";
		}

		List<OrderProductDto> opds = new ArrayList<OrderProductDto>();
		for (int i = 0; i < nameArray.length; i++) {
			opds.add(new OrderProductDto(productRepository.findByName(nameArray[i]), quantityArray[i]));
		}
		Order order = new Order();
		order = orderService.create(order);
		List<OrderProduct> orderProducts = new ArrayList<>();
		for (OrderProductDto dto : opds) {
			Product product = productRepository.findById(dto.getProduct().getId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + dto.getProduct().getId()));
			orderProducts.add(orderProductService.create(new OrderProduct(order, product, dto.getProductQuantity())));
		}
		order.setOrderProducts(orderProducts);
		if (getCurrentUser()!=null) {order.setUser(getCurrentUser());}
		order.getDeliveryStatus().add(DeliveryStatus.HANDLING);
		order.getPaymentStatus().add(PaymentStatus.NOT_PAID);
		orderService.update(order);

		return "redirect:/product/products";
	}
	
	@GetMapping("/update-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(@PathVariable("id") long id,Model model) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		model.addAttribute("order", order);
		return "order-update";
	}
	
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateOrder(Order order, BindingResult result, Model model) {
		if (result.hasErrors()) {return "order-update";}
		orderRepository.save(order);
		return "order-update";
	}
	
	@GetMapping("/delete-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String delete(@PathVariable("id") long id, Model model) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		orderRepository.delete(order);
		return "redirect:/order/list";
	}
	
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String list(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		return "order-list";
	}

	@GetMapping("/list-user-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String listByUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("orders", user.getOrders());
		return "order-list";
	}

	@GetMapping("/list-personal")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String personalList(Model model) {
		if (getCurrentUser()!=null) {
			model.addAttribute("orders", getCurrentUser().getOrders());
			return "order-list";
		}
		return "error";
	}
	
	@GetMapping("/personal-{id}")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String personalOrder(@PathVariable("id") long id,Model model) {
		if (getCurrentUser()!=null) {
			Order order = orderRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
			if(getCurrentUser().getOrders().contains(order)) {
				model.addAttribute("order", order);
				return "order";
			}
		}
		return "error";
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String getOrder(@PathVariable("id") long id,Model model) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		model.addAttribute("order", order);
		return "order";
	}

	private Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	
	private User getCurrentUser() {
		Object principal = getCurrentAuthentication().getPrincipal();
		if (principal instanceof User) {
			Long id = ((User) getCurrentAuthentication().getPrincipal()).getId();
			return userRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			}
		return null;
	  }
	 

}
