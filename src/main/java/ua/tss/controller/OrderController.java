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

import ua.tss.model.DeliveryDetails;
import ua.tss.model.Order;
import ua.tss.model.OrderProduct;
import ua.tss.model.Product;
import ua.tss.model.User;
import ua.tss.model.dto.OrderProductDto;
import ua.tss.model.enums.DeliveryStatus;
import ua.tss.model.enums.PaymentStatus;
import ua.tss.service.DeliveryDetailsService;
import ua.tss.service.OrderProductService;
import ua.tss.service.OrderService;
import ua.tss.service.ProductService;
import ua.tss.service.UserService;

@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	private DeliveryDetailsService deliveryDetailsService;

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
    private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String create(Model model, @RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray) {

		if (nameArray.length != quantityArray.length) {return "error";}

		List<OrderProductDto> opds = new ArrayList<OrderProductDto>();
		for (int i = 0; i < nameArray.length; i++) {
			opds.add(new OrderProductDto(productService.findByName(nameArray[i]), quantityArray[i]));
		}

		Order order = orderService.create(new Order());
		List<OrderProduct> orderProducts = new ArrayList<>();
		for (OrderProductDto dto : opds) {
			Product product = productService.findById(dto.getProduct().getId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + dto.getProduct().getId()));
			orderProducts.add(orderProductService.create(new OrderProduct(order, product, dto.getProductQuantity())));
		}
		order.setOrderProducts(orderProducts);

		if (getCurrentUser()!=null) {
			order.setUser(getCurrentUser());
			order.setDeliveryDetails(deliveryDetailsService.create(new DeliveryDetails(order)));
		}

		order.getDeliveryStatus().add(DeliveryStatus.HANDLING);
		order.getPaymentStatus().add(PaymentStatus.NOT_PAID);

		orderService.update(order);

		return "redirect:/product/products";
	}

	@GetMapping("/update-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(@PathVariable("id") long id,Model model) {
		Order order = orderService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));

		model.addAttribute("order", order);
		model.addAttribute("deliveryDetails", order.getDeliveryDetails());
		model.addAttribute("orderProducts", order.getOrderProducts());
		return "order-update";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateOrder(Order order,BindingResult result, Model model) {
		if (result.hasErrors()) {return "order-update";}

		Order orderFromDB = orderService.findById(order.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + order.getId()));

		orderFromDB.setDeliveryStatus(order.getDeliveryStatus());
		orderFromDB.setPaymentStatus(order.getPaymentStatus());

		orderService.update(orderFromDB);

		return "redirect:/order/update-"+order.getId();
	}

	@PostMapping("/update-deliveryDetails")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateDeliveryDetails(DeliveryDetails deliveryDetails) {
		deliveryDetailsService.update(deliveryDetails);
		orderService.update(deliveryDetails.getOrder());
		return "redirect:/order/update-"+deliveryDetails.getOrder().getId();

	}
	@PostMapping("/update-products")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String updateProducts(Model model,
			@RequestParam("orderUPId") Long orderUPId,
			@RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray) {

		if (nameArray.length != quantityArray.length) {return "error";}
		Order order = orderService.findById(orderUPId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + orderUPId));
		List<OrderProductDto> dtos = new ArrayList<OrderProductDto>();
		for (int i = 0; i < nameArray.length; i++) {
			dtos.add(new OrderProductDto(productService.findByName(nameArray[i]), quantityArray[i]));
		}
		List<OrderProduct> orderProducts = new ArrayList<>();
		for (OrderProductDto dto : dtos) {
			Product product = productService.findById(dto.getProduct().getId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + dto.getProduct().getId()));
			orderProducts.add(orderProductService.create(new OrderProduct(order, product, dto.getProductQuantity())));
		}
		order.setOrderProducts(orderProducts);
		orderService.update(order);
		return "redirect:/order/update-"+order.getId();
	}



	@GetMapping("/delete-{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String delete(@PathVariable("id") long id, Model model) {
		Order order = orderService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
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
			Order order = orderService.findById(id)
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
		Order order = orderService.findById(id)
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
			return userService.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			}
		return null;
	  }


}
