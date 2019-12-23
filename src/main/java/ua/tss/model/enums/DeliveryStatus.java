package ua.tss.model.enums;

public enum DeliveryStatus {
	HANDLING("HANDLING"),
	ON_DELIVERY("ON_DELIVERY"),
	DELIVERED("DELIVERED");
	
	private String status;

	DeliveryStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
