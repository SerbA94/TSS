package ua.tss.model.dto;

import ua.tss.model.Product;

public class OrderProductDto {
 
	private Product product;
    private Integer productQuantity;
    
    


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
    
    
    
 
    
}
