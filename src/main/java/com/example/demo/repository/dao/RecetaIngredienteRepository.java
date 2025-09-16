package com.example.demo.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.repository.entity.RecetaIngrediente;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface RecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, Long>{

	@Query(value = "SELECT i FROM RecetaIngrediente i WHERE i.receta.id = :idreceta")
	public List<RecetaIngrediente> findAllByIdRecipe(@Param("idreceta") Long idReceta);

	@Query(value = "SELECT i FROM RecetaIngrediente i WHERE i.receta.id = :idreceta AND i.ingrediente.id = :idingrediente")
	public Optional<RecetaIngrediente> findByRecetaIngredienteId(@Param("idreceta") Long idreceta, @Param("idingrediente") Long idingrediente);

	@Transactional
    @Modifying
    @Query("DELETE FROM RecetaIngrediente i WHERE i.receta.id = :idReceta")
	public void deleteByIdReceta(Long idReceta);
}
