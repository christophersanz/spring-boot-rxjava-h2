package com.bolsadeideas.springboot.rxjava.app.controllers;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.rxjava.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Producto;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);

	@Autowired
	private ProductoDao dao;
	
	@GetMapping
	public Flowable<Producto> index(){
		/*Flowable<Flowable> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).doOnNext(prod -> log.info(prod.getNombre()));*/
		
		//Flowable<Producto> productos = Flowable.defer(() -> Flowable.fromIterable(dao.findAll()));
		Flowable<Producto> productos = Flowable.defer(() -> Flowable.fromIterable(dao.findAll()).map(producto -> {
			producto.setNombre(producto.getNombre().toLowerCase());
			return producto;
		}));
		
		return productos;
	}
	
	@GetMapping("/{id}")
	public Maybe<Optional<Producto>> show(@PathVariable String id){
		/*Maybe<Producto> producto = dao.findById(id);*/
		Maybe<Optional<Producto>> producto = Maybe.just(dao.findById(id));
		return producto;
	}
	
	@PostMapping
	public void save(@RequestBody Producto prod){
		/*Maybe.just(prod)
        .flatMapSingle(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
        }).subscribe(product -> log.info("Insert: " + product.getId() + " " + product.getNombre()));*/
		
		Maybe.just(prod)
        .map(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
        }).subscribe(product -> log.info("Insert: " + product.getId() + " " + product.getNombre()));
	}
	
	@PutMapping("/{id}")
	public void delete(@PathVariable String id){
		dao.deleteById(id);
	}

}
