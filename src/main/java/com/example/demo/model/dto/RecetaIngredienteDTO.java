package com.example.demo.model.dto;

import java.io.Serializable;

import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.RecetaIngrediente;
import com.example.demo.repository.entity.Usuario;

import lombok.Data;

@Data
public class RecetaIngredienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String cantidad;
	private RecetaDTO recetaDTO;
	private IngredienteDTO ingredienteDTO;

	public static RecetaIngredienteDTO convertToDTO(RecetaIngrediente ri) {
		
		RecetaIngredienteDTO riDTO = new RecetaIngredienteDTO();
		riDTO.setId(ri.getId());
		riDTO.setCantidad(ri.getCantidad());
		riDTO.setIngredienteDTO(IngredienteDTO.convertToDTO(ri.getIngrediente()));
		riDTO.setRecetaDTO(RecetaDTO.convertToDTO(ri.getReceta()));
		
		return riDTO;
	}

	public static RecetaIngrediente convertToEntity(RecetaIngredienteDTO riDTO) {
		
		RecetaIngrediente ri = new RecetaIngrediente();
		ri.setId(riDTO.getId());
		ri.setCantidad(riDTO.getCantidad());
		ri.setIngrediente(IngredienteDTO.convertToEntity(riDTO.getIngredienteDTO()));
		//Pais p = PaisDTO.convertToEntity(riDTO.getRecetaDTO().getUsuarioDTO().getPaisDTO());
		Usuario u = UsuarioDTO.convertToEntityUsu(riDTO.getRecetaDTO().getUsuarioDTO());
		ri.setReceta(RecetaDTO.convertToEntity(riDTO.getRecetaDTO(), u));
		
		return ri;
	}
}