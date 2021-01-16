package com.bolsadeideas.springboot.rxjava.app.models.documents;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasas")
@Getter @Setter
public class Rate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Rate(){}
	
	public Rate(String description, Float rate) {
		this.description = description;
		this.rate = rate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private String description;
	private Float rate;
	private Date createAt;

}
