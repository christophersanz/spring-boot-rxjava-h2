package com.bolsadeideas.springboot.rxjava.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.rxjava.app.models.documents.Producto;

	//public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {
	//public interface ProductoDao extends RxJava2CrudRepository<Producto, String> {
	public interface ProductoDao extends JpaRepository<Producto, String> {	
		
}
