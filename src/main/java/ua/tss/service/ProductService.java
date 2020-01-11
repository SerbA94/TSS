package ua.tss.service;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tss.model.Product;
import ua.tss.repository.ProductRepository;


@Service
@Transactional
public class ProductService{

    @Autowired
	private ProductRepository productRepository;

    public Product create(Product product) {
        return this.productRepository.save(product);
    }

    public Iterable<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Product findByName(String name) {
        return this.productRepository.findByName(name);
    }

    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }

}
