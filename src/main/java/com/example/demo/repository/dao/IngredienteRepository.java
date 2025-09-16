package com.example.demo.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Ingrediente;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>{

	@Query(value= "SELECT * FROM ingrediente WHERE nombre = ?1" , nativeQuery=true)
	public Optional <Ingrediente> findByNombre(@Param("nombre") String nombre);

	@Query(value= "SELECT * FROM ingrediente ORDER BY nombre" , nativeQuery=true)
	public List<Ingrediente> findAllOrder();
}
