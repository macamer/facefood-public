package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.repository.dao.PaisRepository;
import com.example.demo.repository.entity.Pais;

@Service
public class PaisServiceImpl implements PaisService {
	private static final Logger log = LoggerFactory.getLogger(PaisServiceImpl.class);
	
	@Autowired
	PaisRepository paisRepository;

	@Override
	public List<PaisDTO> findAll() {
		log.info("PaisServiceImpl - findAll: Lista de todas las ciudades");
		
		List<PaisDTO> listCountriesDTO = paisRepository.findAll()
				.stream()
				.map(p -> PaisDTO.convertToDTO(p))
				.collect(Collectors.toList());
		
		return listCountriesDTO;
	}

	@Override
	public PaisDTO findById(PaisDTO pDTO) {
		log.info("PaisServiceImpl - findById: busca el pais " + pDTO.getId());
		Optional<Pais> pais = paisRepository.findById(pDTO.getId());
		
		if(pais.isPresent()) {
			pDTO = PaisDTO.convertToDTO(pais.get());
			return pDTO;
		} else {
			return null;
		}
	}
	
}
