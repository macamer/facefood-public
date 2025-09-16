package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.PasoDTO;
import com.example.demo.model.dto.RecetaDTO;

public interface PasoService {

	List<PasoDTO> findAllByIdRecipe(RecetaDTO recetaDTO);

	void save(PasoDTO pasoDTO);

	PasoDTO findPasoById(Long id);

	void deleteAllByRecetaId(Long idReceta);
}
