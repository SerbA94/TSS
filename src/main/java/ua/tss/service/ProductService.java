package ua.tss.service;

import org.springframework.stereotype.Service;

import ua.tss.repository.ProductRepository;

@Service
public class ProductService{
	
	private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
   
}
