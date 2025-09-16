package com.example.demo.service;

import com.example.demo.model.dto.FavoritosDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;

public interface FavoritosService {

	void delete(FavoritosDTO fDTO);

	void save(FavoritosDTO fDTO);

	Boolean isRecetaUsuarioFav(RecetaDTO recetaDTO, UsuarioDTO uDTO);

}
