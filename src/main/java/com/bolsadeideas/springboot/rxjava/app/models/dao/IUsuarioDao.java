package com.bolsadeideas.springboot.rxjava.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.rxjava.app.models.documents.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, String>{

	public Usuario findByUsername(String username);

}
