package ua.tss.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.tss.model.DeliveryDetails;
import ua.tss.model.Order;
import ua.tss.model.OrderItem;
import ua.tss.model.User;
import ua.tss.model.enums.DeliveryStatus;
import ua.tss.model.enums.PaymentStatus;
import ua.tss.service.DeliveryDetailsService;
import ua.tss.service.OrderItemService;
import ua.tss.service.OrderService;
import ua.tss.service.ProductService;
import ua.tss.service.UserService;

@Controller
@RequestMapping("order")
public class OrderController extends SuperController {

	@Autowired
	private DeliveryDetailsService deliveryDetailsService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String create(Model model, @RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray, DeliveryDetails dd) {


		// validate deliveryDetails, if is not valid - reset import form

		if (nameArray.length != quantityArray.length) {
			return "error";
		}

		Order order = orderService.create(new Order());

		DeliveryDetails deliveryDetails = deliveryDetailsService.create(new DeliveryDetails());
		if(dd!=null){
			deliveryDetails.copyAllDetails(dd);
		}else {

			deliveryDetails.copyAllDetails(new DeliveryDetails());
		}

		if (getCurrentUser(userService) != null) {
			deliveryDetails.setUser(getCurrentUser(userService));
		}

		deliveryDetailsService.update(deliveryDetails);

		order.setDeliveryDetails(deliveryDetails);

		List<OrderItem> orderItems = new ArrayList<>();
		for (int i = 0; i < nameArray.length; i++) {
			orderItems.add(
					orderItemService.create(new OrderItem(productService.findByName(nameArray[i]), quantityArray[i])));
		}
		order.setOrderItems(orderItems);

		order.getDeliveryStatus().add(DeliveryStatus.HANDLING);
		order.getPaymentStatus().add(PaymentStatus.NOT_PAID);

		orderService.update(order);

		return "redirect:/product/products";
	}

	@GetMapping("/update-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(@PathVariable("id") long id, Model model) {
		Order order = orderService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));

		model.addAttribute("order", order);
		model.addAttribute("deliveryDetails", order.getDeliveryDetails());
		model.addAttribute("orderItems", order.getOrderItems());
		return "order-update";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateOrder(Order order, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "order-update";
		}

		Order orderFromDB = orderService.findById(order.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + order.getId()));

		orderFromDB.setDeliveryStatus(order.getDeliveryStatus());
		orderFromDB.setPaymentStatus(order.getPaymentStatus());

		orderService.update(orderFromDB);

		return "redirect:/order/update-" + order.getId();
	}

	@PostMapping("/update-deliveryDetails")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateDeliveryDetails(DeliveryDetails deliveryDetails, @RequestParam("orderId") Long orderId) {
		deliveryDetailsService.update(deliveryDetails);
		return "redirect:/order/update-" + orderId;
	}

	@PostMapping("/update-products")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateProducts(Model model, @RequestParam("orderUPId") Long orderUPId,
			@RequestParam("nameArray") String[] nameArray, @RequestParam("quantityArray") Integer[] quantityArray) {

		if (nameArray.length != quantityArray.length) {
			return "error";
		}
		Order order = orderService.findById(orderUPId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + orderUPId));

		List<OrderItem> orderItems = new ArrayList<>();
		for (int i = 0; i < nameArray.length; i++) {
			orderItems.add(orderItemService.create(new OrderItem(productService.findByName(nameArray[i]), quantityArray[i])));
		}
		order.setOrderItems(orderItems);
		orderService.update(order);
		return "redirect:/order/update-" + order.getId();
	}

	@GetMapping("/delete-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String delete(@PathVariable("id") long id, Model model) {
		Order order = orderService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		orderService.delete(order);
		return "redirect:/order/list";
	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String list(Model model) {
		model.addAttribute("orders", orderService.getAllOrders());
		return "order-list";
	}

	@GetMapping("/list-user-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String listByUser(@PathVariable("id") long id, Model model) {
		User user = userService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("orders", orderService.getAllUserOrders(user));
		return "order-list";
	}

	@GetMapping("/list-personal")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String personalList(Model model) {
		if (getCurrentUser(userService) != null) {
			model.addAttribute("orders", orderService.getAllUserOrders(getCurrentUser(userService)));
			return "order-list";
		}
		return "error";
	}

	@GetMapping("/personal-{id}")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String personalOrder(@PathVariable("id") long id, Model model) {
		if (getCurrentUser(userService) != null) {
			Order order = orderService.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
			for (Order o : orderService.getAllUserOrders(getCurrentUser(userService))) {
				if(order.equals(o)) {
					model.addAttribute("order", order);
					return "order";
				}
			}
		}
		return "error";
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String getOrder(@PathVariable("id") long id, Model model) {
		Order order = orderService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		model.addAttribute("order", order);
		return "order";
	}

}
