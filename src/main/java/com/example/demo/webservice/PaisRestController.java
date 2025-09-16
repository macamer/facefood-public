package com.example.demo.webservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.service.PaisService;

@RestController
@RequestMapping("/api/paises")
public class PaisRestController {
	
	private static final Logger log = LoggerFactory.getLogger(PaisRestController.class);

	@Autowired
    private PaisService paisService; // Servicio que obtiene los países desde la BD

    @GetMapping
    public ResponseEntity<List<PaisDTO>> obtenerPaises() {
    	log.info("PaisRestController - findAll: Mostramos todos los paises");
        List<PaisDTO> listaPaisesDTO = paisService.findAll();
        return new ResponseEntity<>(listaPaisesDTO, HttpStatus.OK);
    }
    
}
