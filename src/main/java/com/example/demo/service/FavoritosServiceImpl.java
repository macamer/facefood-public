package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.FavoritosDTO;
import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.FavoritosRepository;
import com.example.demo.repository.entity.Favoritos;
import com.example.demo.repository.entity.FavoritosId;
import com.example.demo.repository.entity.Pais;

import jakarta.transaction.Transactional;

@Service
public class FavoritosServiceImpl implements FavoritosService {

	private static final Logger log = LoggerFactory.getLogger(FavoritosServiceImpl.class);

	@Autowired
	private FavoritosRepository favoritosRepository;

	@Override
	@Transactional
	public void delete(FavoritosDTO fDTO) {
		log.info("FavoritosServiceImpl - delete: borramos receta: " + fDTO.getId_receta() + " usuario:"
				+ fDTO.getId_usu());

		favoritosRepository.deleteByUsuarioReceta(fDTO.getId_usu(), fDTO.getId_receta());

	}

	@Override
	@Transactional
	public void save(FavoritosDTO fDTO) {
		log.info("FavoritosServiceImpl - add: añadimos fav receta: " + fDTO.getId_receta() + " usuario:"
				+ fDTO.getId_usu());
		/*
		 * Favoritos fav = new Favoritos(); fav.setId_receta(fDTO.getId_receta());
		 * fav.setId_usu(fDTO.getId_usu());
		 */

		FavoritosId favoritosId = new FavoritosId(fDTO.getId_usu(), fDTO.getId_receta());
		Favoritos fav = new Favoritos(favoritosId);
		favoritosRepository.save(fav);

	}

	@Override
	public Boolean isRecetaUsuarioFav(RecetaDTO recetaDTO, UsuarioDTO uDTO) {
		log.info("FavoritosServiceImpl - isRecetaUsuarioFav: comprobar si es fav: " + recetaDTO.getId() + " usuario:"
				+ uDTO.getId());

		Optional<Favoritos> favOp = favoritosRepository.isRecetaUsuarioFav(recetaDTO.getId(), uDTO.getId());
		Boolean isFavorita = false;
		if (favOp.isPresent()) {
			isFavorita = true;
		}
		return isFavorita;
	}

}
