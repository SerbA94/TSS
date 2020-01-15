package ua.tss.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 5892021103562814232L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Product product;

    private Integer productQuantity;

    public OrderItem() {
		super();
	}

	public OrderItem(Product product, Integer productQuantity) {
		super();
		this.product = product;
		this.productQuantity = productQuantity;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	@Transient
	public Integer getOrderItemPrice() {
		return getProduct().getPrice() * getProductQuantity();
	}

}
