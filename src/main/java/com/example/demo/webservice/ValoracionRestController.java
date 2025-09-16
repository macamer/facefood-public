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

import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.ValoracionDTO;
import com.example.demo.service.RecetaService;
import com.example.demo.service.UserService;
import com.example.demo.service.ValoracionService;

@RestController
@RequestMapping("/api/valoracion")
public class ValoracionRestController {
	
	private static final Logger log = LoggerFactory.getLogger(RecetaRestController.class);

	@Autowired
	private RecetaService recetaService;
	
	@Autowired
	private UserService userService;

	@Autowired 
	private ValoracionService valoracionService;

	
	@GetMapping("/{idUsuario}/{idReceta}/rate")
	public ResponseEntity<ValoracionDTO> findRating(@PathVariable("idReceta") Long idReceta, 
			@PathVariable("idUsuario") Long idUsuario) {
		log.info("ValoracionRestController - findRating: buscar la valoracion entre r:" + idReceta + "y usu " +idUsuario);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);
		uDTO = userService.findById(uDTO);
		
		RecetaDTO rDTO = new RecetaDTO();
		rDTO.setId(idReceta);
		rDTO = recetaService.findById(rDTO);
		
		ValoracionDTO vDTO = valoracionService.findByRecetaUsuario(rDTO, uDTO);

		if (vDTO == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<ValoracionDTO>(vDTO, HttpStatus.OK);
		}
	}
}
