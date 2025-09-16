package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.entity.Receta;

public interface RecetaService {

	List<RecetaDTO> findAll();

	List<RecetaDTO> findMyRecipes(RecetaDTO recetaDTO);

	List<RecetaDTO> findFav(UsuarioDTO usuarioDTO);

	List<RecetaDTO> findByCountry(RecetaDTO recetaDTO);

	List<RecetaDTO> findByWords(String text);

	RecetaDTO findById(RecetaDTO recetaDTO);

	RecetaDTO save(RecetaDTO recetaDTO);

	List<RecetaDTO> findBestRecetas();

	RecetaDTO updateRate(RecetaDTO rDTO, UsuarioDTO uDTO, int rate);

	RecetaDTO update(RecetaDTO recetaDTO);

	void deleteRecetaById(Long idReceta);

	List<RecetaDTO> findNew();

	List<RecetaDTO> findBestRecetasByWordsAndCountry(PaisDTO pDTO, String text);

	List<RecetaDTO> findNewRecetasByWordsAndCountry(PaisDTO pDTO, String text);

	List<RecetaDTO> findByWordsAndCountry(PaisDTO pDTO, String text);
	
	List<RecetaDTO> findBestRecetasByWords(String texto);

	List<RecetaDTO> findNewRecetasByWords(String text);

	List<RecetaDTO> findBestRecetasByCountry(Long idPais);

	List<RecetaDTO> findNewRecetasByCountry(Long idPais);

	List<RecetaDTO> findOld();

	List<RecetaDTO> findOldRecetasByWordsAndCountry(PaisDTO pDTO, String text);
	
	List<RecetaDTO> findOldRecetasByCountry(Long idPais);
	
	List<RecetaDTO> findOldRecetasByWords(String texto);

}
