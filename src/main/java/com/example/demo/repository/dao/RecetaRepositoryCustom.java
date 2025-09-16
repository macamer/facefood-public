package com.example.demo.repository.dao;

import java.util.List;

import com.example.demo.repository.entity.Receta;

public interface RecetaRepositoryCustom {
	List<Receta> findByWords(String texto);
	List<Receta> findBestRecetasByWordsAndCountry(Long idPais, String texto);
	List<Receta> findNewRecetasByWordsAndCountry(Long idPais, String texto);
	List<Receta> findOldRecetasByWordsAndCountry(Long idPais, String texto);
	List<Receta> findByWordsAndCountry(Long idPais, String texto);
	List<Receta> findBestRecetasByWords(String texto);
	List<Receta> findNewRecetasByWords(String texto);
	List<Receta> findOldRecetasByWords(String texto);
}
