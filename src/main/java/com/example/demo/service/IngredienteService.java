package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.IngredienteDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;

public interface IngredienteService {

	List<RecetaIngredienteDTO> findAllByIdRecipe(RecetaDTO recetaDTO);

	List<IngredienteDTO> findAll();

	IngredienteDTO findById(Long id);

	IngredienteDTO save(IngredienteDTO ingredienteDTO);

	IngredienteDTO findByNombre(String string);

	void saveRecetaIngrediente(RecetaIngredienteDTO recetaIngredienteDTO);

	RecetaIngredienteDTO findRecetaIngredienteById(Long id);

	RecetaIngredienteDTO findByRecetaIngredienteId(RecetaDTO recetaDTO, IngredienteDTO ingredienteDTO);

	void deleteAllByRecetaId(Long idReceta);

}
