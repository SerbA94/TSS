package ua.tss.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class Order implements Serializable {

	private static final long serialVersionUID = 5667448666874366510L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

}
