package com.bolsadeideas.springboot.rxjava.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.rxjava.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Producto;

import io.reactivex.Flowable;

@Controller
public class ProductoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoDao dao;
	
	@GetMapping({"/listar", "/"})
	public String listar(Model model) {
		Flowable<Producto> productos = Flowable.defer(() -> Flowable.fromIterable(dao.findAll()).map(producto -> {
			producto.setNombre(producto.getNombre().toLowerCase());
			return producto;
		}));
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
		
		List<Producto> lista = new ArrayList<>();
		productos.subscribe(prod -> lista.add(prod));
		
		model.addAttribute("productos", lista);
		model.addAttribute("titulo", "Listado de productos");
		
		return "listar";
	}
	
	@GetMapping("/listar-datadriver")
	public String listarDataDriver(Model model) {
		Flowable<Producto> productos = Flowable.defer(() -> Flowable.fromIterable(dao.findAll()).map(producto -> {
			producto.setNombre(producto.getNombre().toLowerCase());
			return producto;
		}).delay(1, TimeUnit.MILLISECONDS)); //tiempo que espera en lanzar el lote de registros por segundo -> 9s
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
		
		List<Producto> lista = new ArrayList<>();
		productos.subscribe(prod -> lista.add(prod));
		
		//model.addAttribute("productos", new ReactiveDataDriverContextVariable(lista,2)); //cant de elementos a mostrar por delay
		model.addAttribute("productos", lista); //cant de elementos a mostrar por delay
		model.addAttribute("titulo", "Listado de productos");
		
		return "listar";
	}
	
	@GetMapping("/listar-full")
	public String listarFull(Model model) {
		Flowable<Producto> productos = Flowable.defer(() -> Flowable.fromIterable(dao.findAll()).map(producto -> {
			producto.setNombre(producto.getNombre().toLowerCase());
			return producto;
		}).repeat(5000)); //se repite 5000 veces el flujo
		
		List<Producto> lista = new ArrayList<>();
		productos.subscribe(prod -> lista.add(prod));
		
		model.addAttribute("productos", lista);
		model.addAttribute("titulo", "Listado de productos");
		
		return "listar";
	}
	
}
