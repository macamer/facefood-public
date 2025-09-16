package com.example.demo.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.FavoritosDTO;
import com.example.demo.service.FavoritosService;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritosRestController {
	private static final Logger log = LoggerFactory.getLogger(FavoritosRestController.class);
	
	@Autowired
	private FavoritosService favoritosService;

	// Borrar un cliente
	@DeleteMapping("delete/{idUsuario}/{idReceta}")
	public ResponseEntity<String> delete(@PathVariable("idUsuario") Long idUsuario,
			@PathVariable("idReceta") Long idReceta) {
		log.info("FavoritosRestController - delete: Borramos favoritos la receta" + idReceta + " del usuario:"
				+ idUsuario);

		FavoritosDTO fDTO = new FavoritosDTO();
		fDTO.setId_usu(idUsuario);
		fDTO.setId_receta(idReceta);
		favoritosService.delete(fDTO);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("add/{idUsuario}/{idReceta}")
	public ResponseEntity<FavoritosDTO> add(@PathVariable("idUsuario") Long idUsuario, 
			@PathVariable("idReceta") Long idReceta) {
		log.info("FavoritosRestController - add: Añadimos a favoritos del usuario: " + idUsuario);

		FavoritosDTO fDTO = new FavoritosDTO();
		fDTO.setId_usu(idUsuario);
		fDTO.setId_receta(idReceta);
		
		favoritosService.save(fDTO);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
