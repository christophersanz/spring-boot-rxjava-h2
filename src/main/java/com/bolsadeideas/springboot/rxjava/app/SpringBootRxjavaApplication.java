package com.bolsadeideas.springboot.rxjava.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.rxjava.app.models.dao.IRateDao;
import com.bolsadeideas.springboot.rxjava.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Rate;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Role;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Usuario;
import com.bolsadeideas.springboot.rxjava.app.util.CalendarUtil;

import io.reactivex.Flowable;

@SpringBootApplication
@EnableWebSecurity
public class SpringBootRxjavaApplication implements CommandLineRunner {
	
private static final Logger log = LoggerFactory.getLogger(SpringBootRxjavaApplication.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IRateDao rateDao;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootRxjavaApplication.class, args);
		Flowable.just("Tasas de Hoy:"+CalendarUtil.getDateOperation()).subscribe(System.out::println);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//users
		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(new Usuario("admin", "", true, Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN"))));
		usuarios.add(new Usuario("christopher", "", true, Arrays.asList(new Role("ROLE_USER"))));
		
		String password = "12345";
		for(Usuario usuario: usuarios) {
			String bcryptPassword = passwordEncoder.encode(password);
			usuario.setPassword(bcryptPassword);
			usuarioDao.save(usuario);
			log.info("Insert: " + usuario.getUsername() + " " + usuario.getPassword());
		}
		
		
		//rates
		List<Rate> rates = new ArrayList<>();
		rates.add(new Rate("Dolar", 3.64F));
		rates.add(new Rate("Euro", 4.41F));
		
		Flowable.fromIterable(rates)
		.map(tasa -> {
			tasa.setCreateAt(new Date());
			return rateDao.save(tasa);
        })
		.subscribe(tasa -> log.info("Insert: " + tasa.getId() + " " + tasa.getDescription()));
		
	}

}
