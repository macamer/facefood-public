package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.ValoracionDTO;
import com.example.demo.repository.dao.ValoracionRepository;
import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.Valoracion;

@Service
public class ValoracionServiceImpl implements ValoracionService {

	private static final Logger log = LoggerFactory.getLogger(ValoracionServiceImpl.class);
	
	@Autowired
	ValoracionRepository valoracionRepository;

	@Override
	public ValoracionDTO findByRecetaUsuario(RecetaDTO rDTO, UsuarioDTO uDTO) {

		Optional <Valoracion> vOp = valoracionRepository.findByRecetaUsuario(rDTO.getId(), uDTO.getId());
		Valoracion v = null;
		if(vOp.isPresent()) {
			v = vOp.get();
			ValoracionDTO vDTO = ValoracionDTO.convertToDTO(v, rDTO, uDTO);
			return vDTO;
		} else {
			return null;
		}
	}
}
