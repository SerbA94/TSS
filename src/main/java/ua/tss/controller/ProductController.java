package ua.tss.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Product;
import ua.tss.repository.ProductRepository;

@Controller
@RequestMapping("product")
public class ProductController {
	
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("product", new Product());
		return "product-list";
	}
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@PostMapping("/create")
	public String create(Product product, BindingResult result, Model model,@RequestParam("file") MultipartFile file) {
		if (result.hasErrors()) {
			model.addAttribute("products", productRepository.findAll());
			return "product-list";
		}
		if (productRepository.findByName(product.getName()) != null) {
			FieldError existError = new FieldError("product", "name", "Product Exists!");
			result.addError(existError);
			model.addAttribute("products", productRepository.findAll());
			return "product-list";
		}
		productRepository.save(product);
		return "redirect:/product/list";
	}
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		productRepository.delete(product);
		return "redirect:/product/list";
	}	
}
