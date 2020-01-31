package ua.tss.repository;


import org.springframework.data.repository.CrudRepository;

import ua.tss.model.Cart;


public interface CartRepository extends CrudRepository<Cart, Long>{

	Cart findBySessionId(String sessionId);

}
