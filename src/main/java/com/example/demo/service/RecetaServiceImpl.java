package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.PaisRepository;
import com.example.demo.repository.dao.RecetaRepository;
import com.example.demo.repository.dao.UserRepository;
import com.example.demo.repository.dao.ValoracionRepository;
import com.example.demo.repository.entity.Pais;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.Valoracion;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class RecetaServiceImpl implements RecetaService {

	private static final Logger log = LoggerFactory.getLogger(RecetaServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RecetaRepository recetaRepository;
	
	@Autowired
	PaisRepository paisRepository;
	
	@Autowired
	ValoracionRepository valoracionRepository;

	@Override
	public List<RecetaDTO> findAll() {
		log.info("RecetaServiceImpl - findAll: Lista de todas las recetas");

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findAll().stream().map(p -> RecetaDTO.convertToDTO(p))
				.collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findMyRecipes(RecetaDTO recetaDTO) {
		log.info("RecetaServiceImpl - findMyRecipes: buscamos todas recetas del usuario "
				+ recetaDTO.getUsuarioDTO().getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findMyRecipes(recetaDTO.getUsuarioDTO().getId()).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findFav(UsuarioDTO usuarioDTO) {
		log.info("RecetaServiceImpl - findFav: buscamos recetas favoritas del usuario "
				+ usuarioDTO.getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findFav(usuarioDTO.getId()).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findByCountry(RecetaDTO recetaDTO) {
		log.info("RecetaServiceImpl - findByCountry: buscamos todas recetas del pais "
				+ recetaDTO.getPaisDTO().getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findByCountry(recetaDTO.getPaisDTO().getId()).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	public List<RecetaDTO> findByWords(String text) {
		List<RecetaDTO> listaRecetasDTO = recetaRepository.findByWords(text).stream()
				.map(r -> RecetaDTO.convertToDTO(r)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public RecetaDTO findById(RecetaDTO recetaDTO) {
		log.info("RecetaServiceImpl - findById: buscamos la receta " + recetaDTO.getId());
		
		Optional<Receta> recetaOpt = recetaRepository.findByIdWithIngredients(recetaDTO.getId());
	    Optional<Receta> recetaWithSteps = recetaRepository.findByIdWithSteps(recetaDTO.getId());

	    if (recetaOpt.isPresent()) {
	        Receta receta = recetaOpt.get();
	        
	        recetaWithSteps.ifPresent(r -> receta.setListaPasos(r.getListaPasos()));

	        return RecetaDTO.convertToDTO(receta);
	    }

	    return null;
	}

	@Override
	public RecetaDTO save(RecetaDTO recetaDTO) {
		log.info("RecetaServiceImpl - add "+ recetaDTO.toString());
		
		//obtenemos la entidad usuario
		Usuario u = userRepository.findById(recetaDTO.getUsuarioDTO().getId()).orElse(null);
		if (u == null) {
			log.error("RecetaServiceImpl - add: No se ha encontrado el usuario con id " + recetaDTO.getUsuarioDTO().getId());
			return null;
		}
		Pais p = paisRepository.findById(recetaDTO.getPaisDTO().getId()).orElse(null);
		if (p == null) {
			log.error("RecetaServiceImpl - add: No se ha encontrado el pais con id " + recetaDTO.getPaisDTO().getId());
			return null;
		}

		Receta receta = null;
		if(recetaDTO.getId() != null) {
			//guardamos la receta
			Optional <Receta> recetaOP = recetaRepository.findById(recetaDTO.getId());
			if (recetaOP.isPresent()) {
				receta = recetaOP.get();
				
			}
		} else {
			receta = new Receta();
		}
		receta.setTitulo(recetaDTO.getTitulo());
		receta.setDuracion(recetaDTO.getDuracion());
		receta.setNumPersonas(recetaDTO.getNumPersonas());
		receta.setFoto(recetaDTO.getFoto());
		
		//Pais p = PaisDTO.convertToEntity(recetaDTO.getPaisDTO());
		receta.setPais(p);
		
		//Usuario u = UsuarioDTO.convertToEntityUsu(recetaDTO.getUsuarioDTO());
		receta.setUsuario(u);
		
		receta.setExplicacion(recetaDTO.getExplicacion());
		receta.setPuntuacion(recetaDTO.getPuntuacion());
		receta.setFecha(recetaDTO.getFecha());
		
		recetaRepository.save(receta);
		return RecetaDTO.convertToDTO(receta);
	}

	@Override
	public List<RecetaDTO> findBestRecetas() {
		log.info("RecetaServiceImpl - findBestRecetas: Lista mejores recetas");

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findBestRecetas().stream().map(p -> RecetaDTO.convertToDTO(p))
				.collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Transactional
	@Override
	public RecetaDTO updateRate(RecetaDTO rDTO, UsuarioDTO uDTO, int rate) {
		log.info("RecetaServiceImpl - updateRate: actualizar puntuación receta");
		
		Optional <Valoracion> vOP = valoracionRepository.findByRecetaUsuario(rDTO.getId(), uDTO.getId());
		Valoracion v = new Valoracion();
		
		if(vOP.isPresent()) {
			v = vOP.get();
			v.setCalificacion(rate);
			v.setFecha(new Date());
		} else {
			v.setCalificacion(rate);
			v.setFecha(new Date());
			
			Usuario u = userRepository.findById(uDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + uDTO.getId()));
			
			v.setUsuario(u);
			v.setReceta(RecetaDTO.convertToEntity(rDTO, u));
		}
		
		valoracionRepository.save(v);
		
		valoracionRepository.flush();
		recetaRepository.updateRate(rDTO.getId());
		recetaRepository.flush();
		
		// RECARGAR receta actualizada
		Receta recetaActualizada = recetaRepository.findById(rDTO.getId())
		    .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada con ID: " + rDTO.getId()));
		

		return RecetaDTO.convertToDTO(recetaActualizada);
	}

	@Override
	public RecetaDTO update(RecetaDTO recetaDTO) {
		log.info("RecetaServiceImpl - update: actualizamos receta " + recetaDTO.getId());

		Pais p = PaisDTO.convertToEntity(recetaDTO.getUsuarioDTO().getPaisDTO());
		Usuario u = UsuarioDTO.convertToEntity(recetaDTO.getUsuarioDTO(), p);
	    Receta receta = RecetaDTO.convertToEntity(recetaDTO, u); 
	    receta.setPais(PaisDTO.convertToEntity(recetaDTO.getPaisDTO()));

	    Receta recetaActualizada = recetaRepository.save(receta);

	    return RecetaDTO.convertToDTO(recetaActualizada);
	}

	@Transactional
	@Override
	public void deleteRecetaById(Long idReceta) {
		if (!recetaRepository.existsById(idReceta)) {
	        throw new RuntimeException("Receta no encontrada");
	    }
	    recetaRepository.deleteById(idReceta);	}

	@Override
	public List<RecetaDTO> findNew() {
		log.info("RecetaServiceImpl - findBestRecetas: Lista mejores recetas");

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findNewRecetas().stream().map(p -> RecetaDTO.convertToDTO(p))
				.collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	@Override
	public List<RecetaDTO> findOld() {
		log.info("RecetaServiceImpl - findBestRecetas: Lista mejores recetas");

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findOldRecetas().stream().map(p -> RecetaDTO.convertToDTO(p))
				.collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findBestRecetasByWordsAndCountry(PaisDTO pDTO, String text) {
		log.info("RecetaServiceImpl - findByCountry: buscamos las mejores recetas de la búsqueda " + text + " y país "
				+ pDTO.getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findBestRecetasByWordsAndCountry(pDTO.getId(), text).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findNewRecetasByWordsAndCountry(PaisDTO pDTO, String text) {
		log.info("RecetaServiceImpl - findNewRecetasByWordsAndCountry: buscamos las recetas recientes de la búsqueda " + text + " y país "
				+ pDTO.getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findNewRecetasByWordsAndCountry(pDTO.getId(), text).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	@Override
	public List<RecetaDTO> findOldRecetasByWordsAndCountry(PaisDTO pDTO, String text) {
		log.info("RecetaServiceImpl - findNewRecetasByWordsAndCountry: buscamos las recetas recientes de la búsqueda " + text + " y país "
				+ pDTO.getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findOldRecetasByWordsAndCountry(pDTO.getId(), text).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findByWordsAndCountry(PaisDTO pDTO, String text) {
		log.info("RecetaServiceImpl - findByWordsAndCountry: buscamos las recetas de la búsqueda " + text + " y país "
				+ pDTO.getId());

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findByWordsAndCountry(pDTO.getId(), text).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findBestRecetasByWords(String texto) {
		log.info("RecetaServiceImpl - findBestRecetasByWords: buscamos las recetas de la búsqueda " + texto );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findBestRecetasByWords(texto).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	@Override
	public List<RecetaDTO> findNewRecetasByWords(String texto) {
		log.info("RecetaServiceImpl - findBestRecetasByWords: buscamos las recetas de la búsqueda " + texto );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findNewRecetasByWords(texto).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	@Override
	public List<RecetaDTO> findOldRecetasByWords(String texto) {
		log.info("RecetaServiceImpl - findBestRecetasByWords: buscamos las recetas de la búsqueda " + texto );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findOldRecetasByWords(texto).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findBestRecetasByCountry(Long idPais) {
		log.info("RecetaServiceImpl - findBestRecetasByCountry: buscamos las mejores recetas del pais " + idPais );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findBestRecetasByCountry(idPais).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

	@Override
	public List<RecetaDTO> findNewRecetasByCountry(Long idPais) {
		log.info("RecetaServiceImpl - findNewRecetasByCountry: buscamos las recetas recientes del pais " + idPais );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findNewRecetasByCountry(idPais).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}
	
	@Override
	public List<RecetaDTO> findOldRecetasByCountry(Long idPais) {
		log.info("RecetaServiceImpl - findNewRecetasByCountry: buscamos las recetas recientes del pais " + idPais );

		List<RecetaDTO> listaRecetasDTO = recetaRepository.findOldRecetasByCountry(idPais).stream()
				.map(p -> RecetaDTO.convertToDTO(p)).collect(Collectors.toList());

		return listaRecetasDTO;
	}

}
