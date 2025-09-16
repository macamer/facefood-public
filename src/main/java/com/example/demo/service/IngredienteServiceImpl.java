package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.IngredienteDTO;
import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;
import com.example.demo.repository.dao.IngredienteRepository;
import com.example.demo.repository.dao.RecetaIngredienteRepository;
import com.example.demo.repository.entity.Ingrediente;
import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.RecetaIngrediente;

@Service
public class IngredienteServiceImpl implements IngredienteService {

	private static final Logger log = LoggerFactory.getLogger(IngredienteServiceImpl.class);

	@Autowired
	IngredienteRepository ingredienteRepository;

	@Autowired
	RecetaIngredienteRepository recetaIngredienteRepository;

	@Override
	public List<RecetaIngredienteDTO> findAllByIdRecipe(RecetaDTO recetaDTO) {
		log.info(log.getName()+ " - findByIdRecipe: busca los ingredientes de " + recetaDTO.getId());

		List<RecetaIngrediente> lista = recetaIngredienteRepository.findAllByIdRecipe(recetaDTO.getId());

		List<RecetaIngredienteDTO> listaRecetaIngredientesDTO = new ArrayList<RecetaIngredienteDTO>();
		for (int i = 0; i < lista.size(); ++i) {
			listaRecetaIngredientesDTO.add(RecetaIngredienteDTO.convertToDTO(lista.get(i)));
		}

		return listaRecetaIngredientesDTO;
	}

	@Override
	public List<IngredienteDTO> findAll() {
		log.info(log.getName()+ " - findAll: Lista de todas las recetas");

		List<IngredienteDTO> listaIngredientesDTO = ingredienteRepository.findAllOrder().stream().map(p -> IngredienteDTO.convertToDTO(p))
				.collect(Collectors.toList());

		return listaIngredientesDTO;
	}

	@Override
	public IngredienteDTO findById(Long id) {
		log.info(log.getName()+ " - findById: busca el ingrediente " + id);
		Optional<Ingrediente> ing = ingredienteRepository.findById(id);
		
		if(ing.isPresent()) {
			IngredienteDTO ingDTO = IngredienteDTO.convertToDTO(ing.get());
			return ingDTO;
		} else {
			return null;
		}
	}

	@Override
	public IngredienteDTO save(IngredienteDTO ingredienteDTO) {
		log.info(log.getName()+ " - findById: guarda la recetaIngrediente " + ingredienteDTO.toString());
		
		Ingrediente ingrediente = IngredienteDTO.convertToEntity(ingredienteDTO);
		ingredienteRepository.save(ingrediente);
		
		return IngredienteDTO.convertToDTO(ingrediente);
	}
	
	@Override
	public void saveRecetaIngrediente(RecetaIngredienteDTO recetaIngredienteDTO) {
		log.info(log.getName()+ " - saveRecetaIngrediente: guarda la recetaIngrediente " + recetaIngredienteDTO.toString());
		
		
		
		RecetaIngrediente recetaIngrediente = RecetaIngredienteDTO.convertToEntity(recetaIngredienteDTO);
		recetaIngredienteRepository.save(recetaIngrediente);
	}

	@Override
	public IngredienteDTO findByNombre(String string) {
		Optional<Ingrediente> iOp = ingredienteRepository.findByNombre(string);
		
		if(iOp.isPresent()) {
			Ingrediente i = iOp.get();
			return IngredienteDTO.convertToDTO(i);
		} else {
			return null;
		}
	}

	@Override
	public RecetaIngredienteDTO findRecetaIngredienteById(Long id) {
		log.info(log.getName()+ " - findRecetaIngredienteById: buscar la recetaIngrediente " + id);
		
		Optional <RecetaIngrediente> riOp = recetaIngredienteRepository.findById(id);
		RecetaIngrediente ri = new RecetaIngrediente();
		if (riOp.isPresent()) {
			ri = riOp.get();
			
			RecetaIngredienteDTO riDTO = RecetaIngredienteDTO.convertToDTO(ri);
			return riDTO;
		} else {
			return null;
		}
	}

	@Override
	public RecetaIngredienteDTO findByRecetaIngredienteId(RecetaDTO recetaDTO, IngredienteDTO ingredienteDTO) {
		log.info(log.getName()+ " - findRecetaIngredienteId: buscar la recetaIngrediente " + recetaDTO.getId() + " y " + ingredienteDTO.getId());
		
		Optional <RecetaIngrediente> riOp =  recetaIngredienteRepository.findByRecetaIngredienteId(recetaDTO.getId(), ingredienteDTO.getId());
		if(riOp.isPresent()) {
			RecetaIngrediente ri = riOp.get();
			return RecetaIngredienteDTO.convertToDTO(ri);
		}
		
		return null;
	}

	@Override
	public void deleteAllByRecetaId(Long idReceta) {
		recetaIngredienteRepository.deleteByIdReceta(idReceta);
		
	}
}
