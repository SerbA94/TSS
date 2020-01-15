package ua.tss.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;


@Entity
public class Image implements Serializable {

	private static final long serialVersionUID = -3238260504255117805L;
    private static final String type = "image/jpeg";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String name;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;


	public Image() {}


	public Image(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}

	public Image(String name, byte[] data,Product product) {
		this.name = name;
		this.data = data;
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public static String getType() {
		return type;
	}

}
