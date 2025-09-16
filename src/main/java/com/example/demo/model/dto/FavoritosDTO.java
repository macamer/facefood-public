package com.example.demo.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FavoritosDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id_usu;
    private Long id_receta;

    private UsuarioDTO usuarioDTO;

    private RecetaDTO recetaDTO;
}
