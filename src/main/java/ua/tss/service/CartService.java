package ua.tss.service;

import java.util.Optional;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tss.model.Cart;
import ua.tss.repository.CartRepository;

@Service
@Transactional
public class CartService implements HttpSessionListener  {

	@Autowired
	private CartRepository cartRepository;

	public Cart create(Cart cart) {
        return this.cartRepository.save(cart);
    }

    public void update(Cart cart) {
        this.cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return this.cartRepository.findById(id);
    }

    public Cart findBySessionId(String sessionId) {
        return this.cartRepository.findBySessionId(sessionId);
    }

    public Iterable<Cart> getAllCarts() {
        return this.cartRepository.findAll();
    }

    public void delete(Cart cart) {
        this.cartRepository.delete(cart);
    }

    @Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("Session Created Event Handler : " + se.getSession().getId());

		Cart cart = create(new Cart());
		String sessionId = se.getSession().getId();
		cart.setSessionId(sessionId);
		update(cart);

		System.out.println("Cart added to DB: "+cart.getSessionId());

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("Session Destroyed Event Handler : " + se.getSession().getId());
		Cart cart = findBySessionId(se.getSession().getId());
		if(cart==null) {
			System.out.println("NO such cart in DB : " + se.getSession().getId());
		}else {
			delete(cart);
			System.out.println("Cart removed from DB : "+se.getSession().getId());
		}
	}


}
