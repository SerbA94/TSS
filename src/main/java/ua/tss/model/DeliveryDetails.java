package ua.tss.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	private String region;
	private String district;
	private String city;
	private int postNumber;

	@OneToOne
	@JoinColumn(name = "user_userId", referencedColumnName = "id")
	private User user;

	public DeliveryDetails() {
		super();
	}


	public void copyAllDetails(DeliveryDetails dd) {

		this.firstName = dd.getFirstName()!=null ? dd.getFirstName():"custom";
		this.lastName = dd.getLastName()!=null?dd.getLastName():"custom";
		this.middleName = dd.getMiddleName()!=null?dd.getMiddleName():"custom";
		this.region = dd.getRegion()!=null?dd.getRegion():"custom";
		this.district = dd.getDistrict()!=null?dd.getDistrict():"custom";
		this.city = dd.getCity()!=null?dd.getCity():"custom";
		this.postNumber = dd.getPostNumber();
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

	public int getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(int postNumber) {
		this.postNumber = postNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
