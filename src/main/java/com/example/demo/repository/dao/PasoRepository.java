package com.example.demo.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Paso;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface PasoRepository extends JpaRepository<Paso, Long>{

	@Query(value = "SELECT p FROM Paso p WHERE p.receta.id = :idreceta")
	public List<Paso> findAllByIdRecipe(@Param("idreceta") Long idReceta);
	
	@Modifying
	@Query(value= "DELETE FROM paso WHERE id_receta = ?1" , nativeQuery=true)
	public void deleteByRecetaId(Long recetaId);
}

