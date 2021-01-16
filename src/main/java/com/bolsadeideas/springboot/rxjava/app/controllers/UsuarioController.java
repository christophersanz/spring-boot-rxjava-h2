package com.bolsadeideas.springboot.rxjava.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.rxjava.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Usuario;

@RestController
public class UsuarioController {
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@PostMapping
	public void save(@RequestBody Usuario usuario) {
		usuarioDao.save(usuario);
	}

}
