package ua.tss.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import ua.tss.model.dto.OrderProductDto;
import ua.tss.repository.ImageRepository;
import ua.tss.repository.ProductRepository;
import ua.tss.service.ImageService;

@Controller
@RequestMapping("product")
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
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String list(Model model) {
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("product", new Product());
		return "product-list";
	}
	
	/* test **** needs update */
	/*#################################################################*/
	@GetMapping("/products")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String products(Model model) {
		List<Product> products = new ArrayList<Product>();
		for (Product product : productRepository.findAll()) {
			if(product.getStock()!=null&&product.getStock()>0) {
				products.add(product);
			}
		}
		model.addAttribute("products", products);
		return "products";
	}
	@PostMapping("/products")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public String productsPost(
			Model model,
			@RequestParam("nameArray") String[] nameArray,
			@RequestParam("quantityArray") Integer[] quantityArray) {
		
		if(nameArray.length!=quantityArray.length) {return "redirect:/";}
		for(int i = 0;i<nameArray.length;i++) {
			System.out.println(nameArray[i]+" - "+quantityArray[i]);
		}
		
		return "redirect:/";
	}
	
	/*#################################################################*/

	
	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(@PathVariable("id") long id,Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("product", product);
		return "product-update";
	}
	
	@PostMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(Product product,BindingResult result,Model model,@RequestParam("files") MultipartFile[] files) {
		if (result.hasErrors()) {
			model.addAttribute("products", productRepository.findAll());
			return "product-list";
		}
		if (productRepository.findByName(product.getName()) == null) {
			FieldError existError = new FieldError("product", "name", "Product is not Exists!");
			result.addError(existError);
			model.addAttribute("products", productRepository.findAll());
			return "product-list";
		}
		productRepository.save(product);
	    Arrays.asList(files).stream().forEach(file -> {
	    	if(!file.isEmpty()) {
				try {
					imageService.storeImage(file,product);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		});
		return "redirect:/product/list";
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
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
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
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
