package ua.tss.model.enums;

public enum PaymentStatus {
	NOT_PAID("NOT_PAID"),
	PAID("PAID"),
	COD("COD");
	
	private String status;

	PaymentStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
