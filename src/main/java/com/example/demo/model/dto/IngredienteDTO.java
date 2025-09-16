package com.example.demo.model.dto;

import java.io.Serializable;

import com.example.demo.repository.entity.Ingrediente;
import lombok.Data;

@Data
public class IngredienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String tipo;

	public static IngredienteDTO convertToDTO(Ingrediente i) {

		IngredienteDTO iDTO = new IngredienteDTO();
		iDTO.setId(i.getId());
		iDTO.setNombre(i.getNombre());
		iDTO.setTipo(i.getTipo());

		return iDTO;
	}

	public static Ingrediente convertToEntity(IngredienteDTO iDTO) {
		Ingrediente i = new Ingrediente();
		i.setId(iDTO.getId());
		i.setNombre(iDTO.getNombre());
		i.setTipo(iDTO.getTipo());

		return i;
	}
}
