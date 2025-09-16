package com.example.demo.model.dto;

import java.io.Serializable;

import com.example.demo.repository.entity.Pais;

import lombok.Data;

@Data
public class PaisDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;

	public static PaisDTO convertToDTO(Pais country) {
		PaisDTO cDTO = new PaisDTO();
		cDTO.setId(country.getId());
		cDTO.setNombre(country.getNombre());
		
		return cDTO;
	}
	
	public static Pais convertToEntity(PaisDTO pDTO) {
		Pais p = new Pais();
		p.setId(pDTO.getId());
		p.setNombre(pDTO.getNombre());
		
		return p;
	}
}
