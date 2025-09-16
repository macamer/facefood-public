package com.example.demo.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.exception.ExceptionMessage;
import com.example.demo.repository.entity.Usuario;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Usuario, Long>{
	
	@Query(value= "SELECT * FROM usuario WHERE usuario = ?1 AND contra = ?2" , nativeQuery=true)
	public Optional <Usuario> findByUserAndPass(String usuario, String contra);
	
	@Query(value= "SELECT * FROM usuario WHERE usuario = ?1" , nativeQuery=true)
	public Optional <Usuario> findByUser(String usuario);
	
	public Optional <Usuario> findByEmail(String email);

	public Optional<Usuario> findByUsuario(String usuario);

}
