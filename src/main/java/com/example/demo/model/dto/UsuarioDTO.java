package com.example.demo.model.dto;

import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.Usuario;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class UsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String apellidos;
	private String email;
	private String contra;
	private String usuario;
	private String avatar;
	private String presentacion;
	
	@ToString.Exclude
	private PaisDTO paisDTO;
	
	@ToString.Exclude
	private List<RecetaDTO> listaRecetasDTO = new ArrayList<>();
	
	public static UsuarioDTO convertToDTO(Usuario user) {
		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(user.getId());
		uDTO.setNombre(user.getNombre());
		uDTO.setApellidos(user.getApellidos());
		uDTO.setEmail(user.getEmail());
		uDTO.setContra(user.getContra());
		uDTO.setUsuario(user.getUsuario());
		uDTO.setAvatar(user.getAvatar());
		uDTO.setPresentacion(user.getPresentacion());
		
		PaisDTO pDTO = PaisDTO.convertToDTO(user.getPais());
		uDTO.setPaisDTO(pDTO);
			
		return uDTO;
	}

	public static Usuario convertToEntity(UsuarioDTO userDTO, Pais pais) {
		Usuario usuario = new Usuario();
		usuario.setId(userDTO.getId());
		usuario.setNombre(userDTO.getNombre());
		usuario.setApellidos(userDTO.getApellidos());
		usuario.setEmail(userDTO.getEmail());
		usuario.setContra(userDTO.getContra());
		usuario.setUsuario(userDTO.getUsuario());
		usuario.setAvatar(userDTO.getAvatar());
		usuario.setPresentacion(userDTO.getPresentacion());

		usuario.setPais(pais);
		
		List<RecetaDTO> listaRecetasDTO = new ArrayList<RecetaDTO>(userDTO.getListaRecetasDTO());
		for (int i = 0; i < listaRecetasDTO.size(); i++) {
			RecetaDTO rDTO = listaRecetasDTO.get(i);
			Receta r = RecetaDTO.convertToEntity(rDTO, usuario);
			usuario.getRecetas().add(r);			
		}
		
		return usuario;
	}
	
	public static Usuario convertToEntityUsu(UsuarioDTO userDTO) {
		Usuario usuario = new Usuario();
		usuario.setId(userDTO.getId());
		usuario.setNombre(userDTO.getNombre());
		usuario.setApellidos(userDTO.getApellidos());
		usuario.setEmail(userDTO.getEmail());
		usuario.setContra(userDTO.getContra());
		usuario.setUsuario(userDTO.getUsuario());
		usuario.setAvatar(userDTO.getAvatar());
		usuario.setPresentacion(userDTO.getPresentacion());
		
		Pais p = PaisDTO.convertToEntity(userDTO.getPaisDTO());
		usuario.setPais(p);
		
		return usuario;
	}
	
}
