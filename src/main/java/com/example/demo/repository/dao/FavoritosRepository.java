package com.example.demo.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.exception.ExceptionMessage;
import com.example.demo.repository.entity.Favoritos;
import com.example.demo.repository.entity.FavoritosId;
import com.example.demo.repository.entity.Usuario;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface FavoritosRepository extends JpaRepository<Favoritos, FavoritosId>{
	
	@Modifying
	@Query(value= "DELETE FROM favoritos WHERE id_usu = ?1 AND id_receta = ?2" , nativeQuery=true)
	public void deleteByUsuarioReceta(Long idUsuario, Long idReceta);

	@Query(value= "SELECT * FROM favoritos WHERE id_usu = ?2 AND id_receta = ?1" , nativeQuery=true)
	public Optional<Favoritos> isRecetaUsuarioFav(Long idReceta, Long idUsuario);

}
