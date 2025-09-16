package com.example.demo.model.dto;

import java.io.Serializable;

import com.example.demo.repository.entity.Paso;
import com.example.demo.repository.entity.Receta;

import lombok.Data;

@Data
public class PasoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private int num;
	private String foto;
	private String explicacion;

    private RecetaDTO recetaDTO;
    
    public static PasoDTO convertToDTO (Paso p) {
    	PasoDTO pDTO = new PasoDTO();
    	pDTO.setId(p.getId());
    	pDTO.setNum(p.getNum());
    	pDTO.setExplicacion(p.getExplicacion());
    	pDTO.setFoto(p.getFoto());
    	
    	RecetaDTO rDTO = RecetaDTO.convertToDTO(p.getReceta());
    	pDTO.setRecetaDTO(rDTO);
    	
    	return pDTO;
    }

	public static Paso convertToEntity(PasoDTO pDTO, Receta r) {
		Paso p = new Paso();
    	p.setId(pDTO.getId());
    	p.setNum(pDTO.getNum());
    	p.setExplicacion(pDTO.getExplicacion());
    	p.setFoto(pDTO.getFoto());
    	
    	p.setReceta(r);
    	
    	return p;
	}
}
