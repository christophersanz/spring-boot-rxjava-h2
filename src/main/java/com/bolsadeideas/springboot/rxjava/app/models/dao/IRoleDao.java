package com.bolsadeideas.springboot.rxjava.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.rxjava.app.models.documents.Usuario;

public interface IRoleDao extends JpaRepository<Usuario, String>{

}
