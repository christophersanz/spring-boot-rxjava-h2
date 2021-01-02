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
@Table(name = "productos")
//@Document(collection="productos")
@Getter @Setter
public class Producto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Producto() {	}
	
	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private String nombre;
	private Double precio;
	private Date createAt;

}
