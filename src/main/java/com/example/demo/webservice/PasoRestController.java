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

import com.example.demo.model.dto.PasoDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;
import com.example.demo.service.IngredienteService;
import com.example.demo.service.PasoService;

@RestController
@RequestMapping("/api/paso")
public class PasoRestController {

	private static final Logger log = LoggerFactory.getLogger(PasoRestController.class);
	
	@Autowired
	private PasoService pasoService;

	@GetMapping("{idRecipe}")
	public ResponseEntity<List<PasoDTO>> findAllByIdRecipe(@PathVariable("idRecipe") Long idRecipe) {
		log.info(log.getClass()+ "- findByIdRecipe: buscamos los pasos de la receta " + idRecipe);
		
		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setId(idRecipe);
		
		List<PasoDTO> listaPasosDTO = pasoService.findAllByIdRecipe(recetaDTO);
		
		if (listaPasosDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaPasosDTO, HttpStatus.OK);
		}
	}
}
