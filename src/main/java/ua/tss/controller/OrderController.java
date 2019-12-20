package ua.tss.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.tss.model.Order;
import ua.tss.model.OrderProduct;
import ua.tss.model.Product;
import ua.tss.model.dto.OrderProductDto;
import ua.tss.repository.ProductRepository;
import ua.tss.service.OrderProductService;
import ua.tss.service.OrderService;

@Controller
@RequestMapping("order")
public class OrderController {
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
	private OrderProductService orderProductService;
	
	@Autowired
	private ProductRepository productRepository;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String create(Model model,@RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray) {
		
		if(nameArray.length!=quantityArray.length) {return "redirect:/";}

		List<OrderProductDto> opds = new ArrayList<OrderProductDto>();
		for(int i = 0;i<nameArray.length;i++) {
			System.out.println(nameArray[i]+" - "+quantityArray[i]);
			opds.add(new OrderProductDto(productRepository.findByName(nameArray[i]),quantityArray[i]));
			
		}
        Order order = new Order();
        order = orderService.create(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto dto : opds) {
        	Product product = productRepository.findById(dto.getProduct().getId())
    				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + dto.getProduct().getId()));
            orderProducts.add(orderProductService.create(new OrderProduct(order,product,dto.getProductQuantity())));
        }
        
        order.setOrderProducts(orderProducts);
        orderService.update(order);
       
		return "redirect:/product/products";
	}

}
