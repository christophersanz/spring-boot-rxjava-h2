package com.bolsadeideas.springboot.rxjava.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.rxjava.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Producto;

import io.reactivex.Flowable;

@SpringBootApplication
public class SpringBootRxjavaApplication implements CommandLineRunner {
	
private static final Logger log = LoggerFactory.getLogger(SpringBootRxjavaApplication.class);
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
//	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRxjavaApplication.class, args);
		Flowable.just("Hello world").subscribe(System.out::println);
	}

	@Override
	public void run(String... args) throws Exception {
//		mongoTemplate.dropCollection("productos").subscribe();
		List<Producto> lista = new ArrayList<>();
		lista.add(new Producto("TV Panasonic Pantalla LCD", 456.89));
		lista.add(new Producto("Sony Camara HD Digital", 177.89));
		lista.add(new Producto("Apple ipod", 46.89));
		lista.add(new Producto("Sony Notebook", 846.89));
		lista.add(new Producto("Hewlett Packard Multifuncional", 200.89));
		lista.add(new Producto("Bianchi Bicicleta", 70.89));
		lista.add(new Producto("HP Notebook Omen 17", 2500.89));
		lista.add(new Producto("Mica Cómoda 5 cajones", 150.89));
		lista.add(new Producto("TV Sony Bravia OLED 4K Ultra HD", 255.89));
		
		Flowable.fromIterable(lista)
		.map(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
        })
		.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
		
	}

}
