package com.example.demo.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.Valoracion;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ValoracionRepository extends JpaRepository<Valoracion, Long>{

	@Query(value= "SELECT * FROM Valoracion WHERE id_receta = ?1 AND id_usuario = ?2" , nativeQuery=true)
	Optional<Valoracion> findByRecetaUsuario(Long idReceta, Long idUsuario);


}
