package ua.tss.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import ua.tss.model.enums.DeliveryStatus;
import ua.tss.model.enums.PaymentStatus;


@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final long serialVersionUID = -8538383382391252333L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @CreatedDate
	@Column(name = "date_created"/* , nullable = false, updatable = false */)
    private LocalDate dateCreated;

    @LastModifiedDate
	@Column(name = "date_updated"/* , nullable = false */)
    private LocalDate dateUpdated;

    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();

	@ElementCollection(targetClass = DeliveryStatus.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "order_deliveryStatus", joinColumns = @JoinColumn(name = "order_id"))
	@Enumerated(EnumType.STRING)
	private Set<DeliveryStatus> deliveryStatus = new LinkedHashSet<>();

	@ElementCollection(targetClass = PaymentStatus.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "order_paymentStatus", joinColumns = @JoinColumn(name = "order_id"))
	@Enumerated(EnumType.STRING)
	private Set<PaymentStatus> paymentStatus = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "deliveryDetails_id", referencedColumnName = "id")
	private DeliveryDetails deliveryDetails;

    @Transient
    public Integer getTotalOrderPrice() {
        int sum = 0;
        for (OrderItem op : getOrderItems()) {
            sum += op.getOrderItemPrice();
        }
        return sum;
    }

    @Transient
    public int getNumberOfProducts() {
    	int sum = 0;
    	for (OrderItem orderItem : this.orderItems) {
			sum+=orderItem.getProductQuantity();
		}
        return sum;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate localDate) {
		this.dateCreated = localDate;
	}

	public LocalDate getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDate dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Set<DeliveryStatus> getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Set<DeliveryStatus> deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Set<PaymentStatus> getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Set<PaymentStatus> paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public DeliveryDetails getDeliveryDetails() {
		return deliveryDetails;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
}
