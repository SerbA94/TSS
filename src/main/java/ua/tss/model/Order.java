package ua.tss.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    private String status;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date dateCreated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", nullable = false)
    @LastModifiedDate
    private Date dateUpdated;
	
    @OneToMany(mappedBy = "pk.order")
    private List<OrderProduct> orderProducts = new ArrayList<>();
    
    @Transient
    public Double getTotalOrderPrice() {
        double sum = 0D;
        List<OrderProduct> orderProducts = getOrderProducts();
        for (OrderProduct op : orderProducts) {
            sum += op.getTotalPrice();
        }

        return sum;
    }
    
    @Transient
    public int getNumberOfProducts() {
        return this.orderProducts.size();
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

}
