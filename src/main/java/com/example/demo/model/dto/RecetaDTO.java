package com.example.demo.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Paso;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.RecetaIngrediente;
import com.example.demo.repository.entity.Usuario;

import jakarta.persistence.Transient;
import lombok.Data;
import lombok.ToString;

@Data
public class RecetaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String titulo;
	private int duracion;
	private int numPersonas;
	private String explicacion;
	private float puntuacion;
	// private String tipo;
	@DateTimeFormat(iso = ISO.DATE)
	private Date fecha;
	private String foto;
	private int visualizaciones;

	@Transient // No se guarda en la BD
	private boolean isFavorita;

	@ToString.Exclude
	private PaisDTO paisDTO;

	@ToString.Exclude
	private UsuarioDTO usuarioDTO;
	@ToString.Exclude
	private List<RecetaIngredienteDTO> listaRecetaIngredientesDTO;
	@ToString.Exclude
	private List<PasoDTO> listaPasosDTO;

	public static RecetaDTO convertToDTO(Receta receta) {
		RecetaDTO rDTO = new RecetaDTO();
		rDTO.setId(receta.getId());
		rDTO.setTitulo(receta.getTitulo());
		rDTO.setDuracion(receta.getDuracion());
		rDTO.setNumPersonas(receta.getNumPersonas());
		rDTO.setExplicacion(receta.getExplicacion());
		rDTO.setPuntuacion(receta.getPuntuacion());
		// rDTO.setTipo (receta.getTipo());
		rDTO.setFecha(receta.getFecha());
		rDTO.setFoto(receta.getFoto());
		rDTO.setVisualizaciones(receta.getVisualizaciones());

		if (receta.getPais() != null) {
			PaisDTO pDTO = PaisDTO.convertToDTO(receta.getPais());
			rDTO.setPaisDTO(pDTO);
		}

		UsuarioDTO uDTO = UsuarioDTO.convertToDTO(receta.getUsuario());
		rDTO.setUsuarioDTO(uDTO);
		
		// Convertir ingredientes
	    if (receta.getListaRecetaIngredientes() != null) {
	        List<RecetaIngredienteDTO> riDTO = receta.getListaRecetaIngredientes().stream()
	            .map(ing -> {
	            	RecetaIngredienteDTO ingDTO = new RecetaIngredienteDTO();
	                IngredienteDTO ingredienteDTO = new IngredienteDTO();
	                ingredienteDTO.setId(ing.getIngrediente().getId());
	                ingredienteDTO.setNombre(ing.getIngrediente().getNombre());
	                ingDTO.setId(ing.getId());
	                ingDTO.setIngredienteDTO(ingredienteDTO);
	                ingDTO.setCantidad(ing.getCantidad());
	                return ingDTO;
	            })
	            .collect(Collectors.toList());
	        rDTO.setListaRecetaIngredientesDTO(riDTO);
	    }

	    // Convertir pasos
	    if (receta.getListaPasos() != null) {
	        List<PasoDTO> pasosDTO = receta.getListaPasos().stream()
	            .map(p -> {
	                PasoDTO pasoDTO = new PasoDTO();
	                pasoDTO.setId(p.getId());
	                pasoDTO.setNum(p.getNum());
	                pasoDTO.setFoto(p.getFoto());
	                pasoDTO.setExplicacion(p.getExplicacion());
	                return pasoDTO;
	            })
	            .collect(Collectors.toList());
	        rDTO.setListaPasosDTO(pasosDTO);
	    }


		return rDTO;
	}

	public static RecetaDTO convertToDTOUsuario(Receta receta, UsuarioDTO uDTO) {
		RecetaDTO rDTO = new RecetaDTO();
		rDTO.setId(receta.getId());
		rDTO.setTitulo(receta.getTitulo());
		rDTO.setDuracion(receta.getDuracion());
		rDTO.setNumPersonas(receta.getNumPersonas());
		rDTO.setExplicacion(receta.getExplicacion());
		rDTO.setPuntuacion(receta.getPuntuacion());
		// rDTO.setTipo (receta.getTipo());
		rDTO.setFecha(receta.getFecha());
		rDTO.setFoto(receta.getFoto());
		rDTO.setVisualizaciones(receta.getVisualizaciones());

		PaisDTO pDTO = PaisDTO.convertToDTO(receta.getPais());
		rDTO.setPaisDTO(pDTO);

		rDTO.setUsuarioDTO(uDTO);
		
		// Convertir ingredientes
	    if (receta.getListaRecetaIngredientes() != null) {
	        List<RecetaIngredienteDTO> riDTO = receta.getListaRecetaIngredientes().stream()
	            .map(ing -> {
	            	RecetaIngredienteDTO ingDTO = new RecetaIngredienteDTO();
	                IngredienteDTO ingredienteDTO = new IngredienteDTO();
	                ingredienteDTO.setId(ing.getIngrediente().getId());
	                ingredienteDTO.setNombre(ing.getIngrediente().getNombre());
	                ingDTO.setIngredienteDTO(ingredienteDTO);
	                ingDTO.setCantidad(ing.getCantidad());
	                return ingDTO;
	            })
	            .collect(Collectors.toList());
	        rDTO.setListaRecetaIngredientesDTO(riDTO);
	    }

	    // Convertir pasos
	    if (receta.getListaPasos() != null) {
	        List<PasoDTO> pasosDTO = receta.getListaPasos().stream()
	            .map(p -> {
	                PasoDTO pasoDTO = new PasoDTO();
	                pasoDTO.setId(p.getId());
	                pasoDTO.setNum(p.getNum());
	                pasoDTO.setFoto(p.getFoto());
	                pasoDTO.setExplicacion(p.getExplicacion());
	                return pasoDTO;
	            })
	            .collect(Collectors.toList());
	        rDTO.setListaPasosDTO(pasosDTO);
	    }

		return rDTO;
	}

	public static Receta convertToEntity(RecetaDTO recetaDTO, Usuario u) {
		Receta r = new Receta();
		r.setId(recetaDTO.getId());
		r.setTitulo(recetaDTO.getTitulo());
		r.setDuracion(recetaDTO.getDuracion());
		r.setNumPersonas(recetaDTO.getNumPersonas());
		r.setExplicacion(recetaDTO.getExplicacion());
		r.setPuntuacion(recetaDTO.getPuntuacion());
		// rDTO.setTipo (receta.getTipo());
		r.setFecha(recetaDTO.getFecha());
		r.setFoto(recetaDTO.getFoto());
		r.setVisualizaciones(recetaDTO.getVisualizaciones());

		Pais p = PaisDTO.convertToEntity(recetaDTO.getPaisDTO());
		r.setPais(p);

		r.setUsuario(u);

		return r;
	}
}
