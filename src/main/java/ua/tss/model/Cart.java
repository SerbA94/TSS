package ua.tss.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Cart implements Serializable {

	private static final long serialVersionUID = -4911321460344604618L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sessionId;

	@OneToMany
	private List<OrderItem> cartItems = new ArrayList<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<OrderItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<OrderItem> cartItems) {
		this.cartItems = cartItems;
	}

	@Transient
	public Integer getCartPrice() {
		int sum = 0;
		for (OrderItem o : getCartItems()) {
			sum += o.getOrderItemPrice();
		}
		return sum;
	}

}
