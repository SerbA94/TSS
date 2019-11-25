package ua.tss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.Product;
import ua.tss.model.User;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	
    Product findByName(String name);

   
}