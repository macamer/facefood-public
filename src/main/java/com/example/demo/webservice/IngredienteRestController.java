package com.example.demo.webservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.IngredienteDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;
import com.example.demo.service.IngredienteService;

@RestController
@RequestMapping("/api/ingrediente")
public class IngredienteRestController {

	private static final Logger log = LoggerFactory.getLogger(IngredienteRestController.class);
	
	@Autowired
	private IngredienteService ingredienteService;

	@GetMapping("{idRecipe}")
	public ResponseEntity<List<RecetaIngredienteDTO>> findAllByIdRecipe(@PathVariable("idRecipe") Long idRecipe) {
		log.info("IngredienteRestController - findById: buscamos los ingredientes de la receta " + idRecipe);
		
		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setId(idRecipe);
		
		List<RecetaIngredienteDTO> listaRecetaIngredienteDTO = ingredienteService.findAllByIdRecipe(recetaDTO);
		
		if (listaRecetaIngredienteDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetaIngredienteDTO, HttpStatus.OK);
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<List<IngredienteDTO>> findAll() {
		log.info("IngredienteRestController - findAll: buscamos todos los ingredientes");
		
		List<IngredienteDTO> listaIngredientesDTO = ingredienteService.findAll();

		if (listaIngredientesDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaIngredientesDTO, HttpStatus.OK);
		}
	}
}
