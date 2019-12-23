package ua.tss.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class DeliveryDetails implements Serializable {

	private static final long serialVersionUID = 1512685222797089839L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstName;
	private String lastName;
	private String middleName;
	private String phoneNumber;
	private String region;
	private String district;
	private String city;
	private Integer postNumber;
	
    @OneToOne(mappedBy = "deliveryDetails")
	private Order order;
	
	
	
	public DeliveryDetails() {
		super();
	}

	public DeliveryDetails(Order order) {
		super();
		this.order = order;
		this.firstName = order.getUser().getFirstName();
		this.lastName = order.getUser().getLastName();
		this.middleName = order.getUser().getMiddleName();
		this.phoneNumber = order.getUser().getPhoneNumber();
		this.region = order.getUser().getRegion();
		this.district = order.getUser().getDistrict();
		this.city = order.getUser().getCity();
		this.postNumber = order.getUser().getPostNumber();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(Integer postNumber) {
		this.postNumber = postNumber;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
	
	

}
