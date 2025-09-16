package com.example.demo.model.dto;

import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.Valoracion;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
public class ValoracionDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private float calificacion;
	private Date fecha;
	
	@ToString.Exclude
	private RecetaDTO recetaDTO;
	
	@ToString.Exclude
	private UsuarioDTO UsuarioDTO;

	public static ValoracionDTO convertToDTO(Valoracion v, RecetaDTO rDTO, com.example.demo.model.dto.UsuarioDTO uDTO) {
		ValoracionDTO vDTO = new ValoracionDTO();
		vDTO.setId(v.getId());
		vDTO.setCalificacion(v.getCalificacion());
		vDTO.setFecha(v.getFecha());
		vDTO.setRecetaDTO(rDTO);
		vDTO.setUsuarioDTO(uDTO);
		
		return vDTO;
	}
}
