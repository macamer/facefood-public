package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.PasoDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.PasoRepository;
import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Paso;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.RecetaIngrediente;
import com.example.demo.repository.entity.Usuario;

@Service
public class PasoServiceImpl implements PasoService {
	private static final Logger log = LoggerFactory.getLogger(IngredienteServiceImpl.class);

	@Autowired
	PasoRepository pasoRepository;

	@Override
	public List<PasoDTO> findAllByIdRecipe(RecetaDTO recetaDTO) {
		log.info(log.getName() + " - findByIdRecipe: busca los pasos de " + recetaDTO.getId());

		List<Paso> lista = pasoRepository.findAllByIdRecipe(recetaDTO.getId());

		List<PasoDTO> listaPasosDTO = new ArrayList<PasoDTO>();
		for (int i = 0; i < lista.size(); ++i) {
			listaPasosDTO.add(PasoDTO.convertToDTO(lista.get(i)));
		}

		return listaPasosDTO;
	}

	@Override
	public void save(PasoDTO pasoDTO) {
		log.info(log.getName()+ " - save: guarda el paso " + pasoDTO.toString());

		Pais pais = PaisDTO.convertToEntity(pasoDTO.getRecetaDTO().getUsuarioDTO().getPaisDTO());
		Usuario u = UsuarioDTO.convertToEntity(pasoDTO.getRecetaDTO().getUsuarioDTO(), pais);
		Receta r = RecetaDTO.convertToEntity(pasoDTO.getRecetaDTO(), u);
		Paso p = PasoDTO.convertToEntity(pasoDTO, r);
		
		pasoRepository.save(p);
	}

	@Override
	public PasoDTO findPasoById(Long id) {
		log.info(log.getName()+ " - findPasoById: guarda el paso " + id);
		
		Optional <Paso> pOp = pasoRepository.findById(id);

		if (pOp.isPresent()) {
			Paso p = pOp.get();
			return PasoDTO.convertToDTO(p);
		}
		
		return null;
	}
	
	@Override
    public void deleteAllByRecetaId(Long recetaId) {
        pasoRepository.deleteByRecetaId(recetaId);
    }

}
