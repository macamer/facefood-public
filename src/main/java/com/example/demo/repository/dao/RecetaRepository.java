package com.example.demo.repository.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.repository.entity.Receta;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface RecetaRepository extends JpaRepository<Receta, Long>, RecetaRepositoryCustom{

	@Query(value="SELECT r FROM Receta r WHERE r.usuario.id = :idu")
	public List<Receta> findMyRecipes(@Param("idu") Long idUsuario);
	
	@Query(value="SELECT r.* FROM receta r JOIN favoritos f ON r.id = f.id_receta WHERE f.id_usu = ?1", nativeQuery = true)
	public List<Receta> findFav(@Param("idu") Long idUsuario);
	
	@Query(value="SELECT r FROM Receta r WHERE r.pais.id = :idp")
	public List<Receta> findByCountry(@Param("idp") Long idPais);
	
	@Query(value = """
		    SELECT r.*
		    FROM receta r
		    ORDER BY (
		        SELECT AVG(v.calificacion) 
		        FROM valoracion v 
		        WHERE v.id_receta = r.id
		    ) DESC,
		    (
		        SELECT COUNT(*) 
		        FROM valoracion v 
		        WHERE v.id_receta = r.id
		    ) DESC
		""", nativeQuery = true)
	public List<Receta> findBestRecetas();
	
	@Modifying
	@Query(value= "UPDATE receta SET puntuacion = (SELECT AVG(calificacion) FROM valoracion WHERE id_receta = receta.id) WHERE id = ?1;", nativeQuery = true)
	public void updateRate(@Param("idr") Long idReceta);
	
	@Query("SELECT r FROM Receta r LEFT JOIN FETCH r.listaRecetaIngredientes WHERE r.id = :idReceta")
	public Optional<Receta> findByIdWithIngredients(@Param("idReceta") Long idReceta);

	@Query("SELECT r FROM Receta r LEFT JOIN FETCH r.listaPasos WHERE r.id = :idReceta")
	public Optional<Receta> findByIdWithSteps(@Param("idReceta") Long idReceta);
	
	@EntityGraph(attributePaths = {"listaRecetaIngredientes", "listaPasos"})
	@Query("SELECT r FROM Receta r WHERE r.id = :idReceta")
	public Optional<Receta> findByIdWithAll(@Param("idReceta") Long idReceta);
		
	@Query(value="SELECT r FROM Receta r ORDER BY fecha DESC")
	public List<Receta> findNewRecetas();
	
	@Query(value="SELECT r FROM Receta r ORDER BY fecha")
	public List<Receta> findOldRecetas();
	
	@Query(value = """
		    SELECT r.*
		    FROM receta r
		    WHERE r.id_pais = ?1
		    ORDER BY (
		        SELECT AVG(v.calificacion) 
		        FROM valoracion v 
		        WHERE v.id_receta = r.id
		    ) DESC,
		    (
		        SELECT COUNT(*) 
		        FROM valoracion v 
		        WHERE v.id_receta = r.id
		    ) DESC
		""", nativeQuery = true)
	public List<Receta> findBestRecetasByCountry(@Param("idPais") Long idPais);
	
	@Query(value="SELECT r FROM Receta r WHERE r.pais.id = :idPais ORDER BY fecha DESC")
	public List<Receta> findNewRecetasByCountry(@Param("idPais") Long idPais);
	
	@Query(value="SELECT r FROM Receta r WHERE r.pais.id = :idPais ORDER BY fecha")
	public List<Receta> findOldRecetasByCountry(@Param("idPais") Long idPais);
	
}
