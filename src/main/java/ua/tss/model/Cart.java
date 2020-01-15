package ua.tss.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

	private static final long serialVersionUID = -4911321460344604618L;


	private String remoteIpAddress;
	private String sessionId;
	private List<OrderItem> cartItems = new ArrayList<>();

}
