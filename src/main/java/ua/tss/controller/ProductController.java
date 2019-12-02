package ua.tss.controller;



import java.io.IOException;
import java.util.Arrays;

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
import ua.tss.repository.ImageRepository;
import ua.tss.repository.ProductRepository;
import ua.tss.service.ImageService;

@Controller
@RequestMapping("product")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
public class ProductController {
	
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Autowired
	private ImageRepository imageRepository;	
	
	@Autowired
    private ImageService imageService;
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("product", new Product());
		return "product-list";
	}
	
	@PostMapping("/create")
	public String create(Product product,BindingResult result,Model model,@RequestParam("files") MultipartFile[] files) {
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
		if(files!=null) {
	         Arrays.asList(files).stream().forEach(file -> {
				try {
					imageService.storeImage(file,product);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		return "redirect:/product/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		if(!product.getImages().isEmpty()&&product.getImages()!=null) {
	        product.getImages().stream().forEach(image -> imageRepository.delete(image));
		}
		productRepository.delete(product);
		return "redirect:/product/list";
	}	
}
