package com.example.demo.webservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.IngredienteDTO;
import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.PasoDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.RecetaIngredienteDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.entity.RecetaIngrediente;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.service.AzureBlobService;
import com.example.demo.service.FavoritosService;
import com.example.demo.service.IngredienteService;
import com.example.demo.service.PaisService;
import com.example.demo.service.PasoService;
import com.example.demo.service.RecetaService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/recetas")
public class RecetaRestController {

	private static final Logger log = LoggerFactory.getLogger(RecetaRestController.class);

	@Autowired
	private RecetaService recetaService;

	@Autowired
	private UserService userService;

	@Autowired
	private PaisService paisService;

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private PasoService pasoService;

	@Autowired
	private FavoritosService favoritosService;

	@Autowired
	private AzureBlobService azureBlobService;

	@GetMapping("/")
	public ResponseEntity<List<RecetaDTO>> findAll(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findAll: buscamos todas las recetas");

		List<RecetaDTO> listaRecetasDTO = recetaService.findAll();

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		Collections.shuffle(listaRecetasDTO);

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/receta/{id}")
	public ResponseEntity<RecetaDTO> getRecipe(@PathVariable Long id) {
		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setId(id);
		recetaDTO = recetaService.findById(recetaDTO);

		if (recetaDTO != null) {
			return ResponseEntity.ok(recetaDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/best")
	public ResponseEntity<List<RecetaDTO>> findBestRecetas(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findBestRecetasByUser: buscamos las mejores recetas del usuario " + idUsuario);

		List<RecetaDTO> listaRecetasDTO = recetaService.findBestRecetas();
		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
	}

	@GetMapping("/new")
	public ResponseEntity<List<RecetaDTO>> findNewRecipes(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findNewRecipesByUser: buscamos más recientes " + idUsuario);

		List<RecetaDTO> listaRecetasDTO = recetaService.findNew();

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
	}

	@GetMapping("/old")
	public ResponseEntity<List<RecetaDTO>> findOldRecipes(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findOldRecipes: buscamos más viejas " + idUsuario);

		List<RecetaDTO> listaRecetasDTO = recetaService.findOld();

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
	}

	@GetMapping("/random")
	public ResponseEntity<List<RecetaDTO>> findRandom(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findBestRecetasByUser: buscamos las mejores recetas del usuario " + idUsuario);

		List<RecetaDTO> listaRecetasDTO = recetaService.findAll();

		// Mezclar aleatoriamente la lista
		Collections.shuffle(listaRecetasDTO);

		// Devolver solo las primeras 4 (o menos si hay menos de 4)
		List<RecetaDTO> recetasLimitadas = listaRecetasDTO.stream().limit(4).collect(Collectors.toList());

		return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
	}

	@PostMapping("/{idUsuario}/{idReceta}/rate")
	public ResponseEntity<RecetaDTO> setRating(@PathVariable("idReceta") Long idReceta,
			@PathVariable("idUsuario") Long idUsuario, @RequestParam("rate") int rate) {
		log.info("RecetaRestController - setRating: puntuar la receta" + idReceta);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);
		uDTO = userService.findById(uDTO);

		RecetaDTO rDTO = new RecetaDTO();
		rDTO.setId(idReceta);
		rDTO = recetaService.findById(rDTO);

		RecetaDTO recetaDTO = recetaService.updateRate(rDTO, uDTO, rate);

		if (recetaDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<RecetaDTO>(recetaDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idUsuario}/fav")
	public ResponseEntity<List<RecetaDTO>> findAllFav(@PathVariable("idUsuario") Long idUsuario) {
		log.info("RecetaRestController - findAll: buscamos todas las recetas y favoritos de " + idUsuario);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);

		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setUsuarioDTO(uDTO);

		List<RecetaDTO> todasLasRecetas = recetaService.findAll();
		Collections.shuffle(todasLasRecetas);
		List<RecetaDTO> recetasFavoritas = recetaService.findFav(uDTO);

		if (todasLasRecetas == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Crear un Set con los IDs de recetas favoritas
		Set<Long> favIds = recetasFavoritas.stream().map(RecetaDTO::getId).collect(Collectors.toSet());

		// Marcar las recetas favoritas en la lista completa
		todasLasRecetas.forEach(receta -> receta.setFavorita(favIds.contains(receta.getId())));
		return new ResponseEntity<>(todasLasRecetas, HttpStatus.OK);
	}

	@GetMapping("/{idUsuario}/my")
	public ResponseEntity<List<RecetaDTO>> findMyRecipes(@PathVariable("idUsuario") Long idUsuario) {
		log.info("RecetaRestController - findMyRecipes: buscamos todas recetas del usuario " + idUsuario);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);

		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setUsuarioDTO(uDTO);
		List<RecetaDTO> listaRecetasDTO = recetaService.findMyRecipes(recetaDTO);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idUsuario}/myfav")
	public ResponseEntity<List<RecetaDTO>> findFav(@PathVariable("idUsuario") Long idUsuario) {
		log.info("RecetaRestController - findMyRecipes: buscamos todas recetas del usuario " + idUsuario);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);

		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setUsuarioDTO(uDTO);
		List<RecetaDTO> recetasFavoritas = recetaService.findFav(uDTO);

		if (recetasFavoritas == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Crear un Set con los IDs de recetas favoritas
		Set<Long> favIds = recetasFavoritas.stream().map(RecetaDTO::getId).collect(Collectors.toSet());

		// Marcar las recetas favoritas en la lista completa
		recetasFavoritas.forEach(receta -> receta.setFavorita(favIds.contains(receta.getId())));

		return new ResponseEntity<>(recetasFavoritas, HttpStatus.OK);

	}

	@GetMapping("/{idPais}/country")
	public ResponseEntity<List<RecetaDTO>> findByCountry(
			@RequestParam(value = "idUsuario", required = false) Long idUsuario, @PathVariable("idPais") Long idPais) {
		log.info("RecetaRestController - findByCountry: buscamos todas recetas del pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setPaisDTO(pDTO);
		List<RecetaDTO> listaRecetasDTO = recetaService.findByCountry(recetaDTO);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
			log.info("RecetaRestController - findByCountry: despues de buscar " + idUsuario);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/text/{text}")
	public ResponseEntity<List<RecetaDTO>> findByWords(@RequestParam("idUsuario") Long idUsuario,
			@PathVariable("text") String text) {
		log.info("RecetaRestController - findByWords: buscamos todas recetas de búsqueda " + text);

		List<RecetaDTO> listaRecetasDTO = recetaService.findByWords(text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/{idPais}/countryBest")
	public ResponseEntity<List<RecetaDTO>> findBestRecetasByWordsAndCountry(@PathVariable("text") String text,
			@PathVariable("idPais") Long idPais, @RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findBestRecetasByWordsAndCountry: buscamos las mejroes recetas de búsqueda "
				+ text + "y pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findBestRecetasByWordsAndCountry(pDTO, text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/{idPais}/countryNew")
	public ResponseEntity<List<RecetaDTO>> findNewRecetasByWordsAndCountry(@PathVariable("text") String text,
			@PathVariable("idPais") Long idPais, @RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findNewRecetasByWordsAndCountry: buscamos las recetas recientes de búsqueda "
				+ text + "y pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findNewRecetasByWordsAndCountry(pDTO, text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/{idPais}/countryOld")
	public ResponseEntity<List<RecetaDTO>> findOldRecetasByWordsAndCountry(@PathVariable("text") String text,
			@PathVariable("idPais") Long idPais, @RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findOldRecetasByWordsAndCountry: buscamos las recetas recientes de búsqueda "
				+ text + "y pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findOldRecetasByWordsAndCountry(pDTO, text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/{idPais}/country")
	public ResponseEntity<List<RecetaDTO>> findByWordsAndCountry(@PathVariable("text") String text,
			@PathVariable("idPais") Long idPais, @RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findByWordsAndCountry: buscamos las recetas de búsqueda " + text + "y pais "
				+ idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findByWordsAndCountry(pDTO, text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/best")
	public ResponseEntity<List<RecetaDTO>> findBestRecetasByWords(@PathVariable("text") String text,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findBestRecetasByWords: buscamos las mejores recetas de búsqueda " + text);

		List<RecetaDTO> listaRecetasDTO = recetaService.findBestRecetasByWords(text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/new")
	public ResponseEntity<List<RecetaDTO>> findNewRecetasByWords(@PathVariable("text") String text,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findNewRecetasByWords: buscamos las recetas recientes de búsqueda " + text);

		List<RecetaDTO> listaRecetasDTO = recetaService.findNewRecetasByWords(text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{text}/text/old")
	public ResponseEntity<List<RecetaDTO>> findOldRecetasByWords(@PathVariable("text") String text,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findOldRecetasByWords: buscamos las recetas recientes de búsqueda " + text);

		List<RecetaDTO> listaRecetasDTO = recetaService.findOldRecetasByWords(text);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idPais}/Countrybest")
	public ResponseEntity<List<RecetaDTO>> findBestRecetasByCountry(@PathVariable("idPais") Long idPais,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findBestRecetasByCountry: buscamos las mejores recetas del pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findBestRecetasByCountry(idPais);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idPais}/CountryNew")
	public ResponseEntity<List<RecetaDTO>> findNewRecetasByCountry(@PathVariable("idPais") Long idPais,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findNewRecetasByCountry: buscamos las mejores recetas del pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findNewRecetasByCountry(idPais);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idPais}/CountryOld")
	public ResponseEntity<List<RecetaDTO>> findOldRecetasByCountry(@PathVariable("idPais") Long idPais,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario) {
		log.info("RecetaRestController - findOldRecetasByCountry: buscamos las mejores recetas del pais " + idPais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(idPais);

		List<RecetaDTO> listaRecetasDTO = recetaService.findOldRecetasByCountry(idPais);

		if (idUsuario != -1) {
			listaRecetasDTO = buscarUserYFavoritos(idUsuario, listaRecetasDTO);
		}

		if (listaRecetasDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(listaRecetasDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{idUsuario}/recipe/{idRecipe}")
	public ResponseEntity<RecetaDTO> findById(@PathVariable("idUsuario") Long idUsuario,
			@PathVariable("idRecipe") Long idRecipe) {
		log.info("RecetaRestController - findById: buscamos la receta " + idRecipe);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);
		uDTO = userService.findById(uDTO);

		RecetaDTO recetaDTO = new RecetaDTO();
		recetaDTO.setId(idRecipe);

		RecetaDTO rDTO = recetaService.findById(recetaDTO);

		if (rDTO != null) {
			// comprobar si es favorita del usuario
			Boolean isFavorita = favoritosService.isRecetaUsuarioFav(recetaDTO, uDTO);
			rDTO.setFavorita(isFavorita);
		}

		if (rDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<RecetaDTO>(rDTO, HttpStatus.OK);
		}
	}

	@PostMapping("{idUsuario}/add")
	public ResponseEntity<RecetaDTO> add(@RequestParam("titulo") String titulo, @RequestParam("idPais") Long idPais,
			@RequestParam("duracion") int duracion, @RequestParam("personas") int personas,
			@RequestParam("presentacion") String presentacion,
			@RequestParam("ingredientes") List<String> listaIngredientes,
			@RequestParam("pasos") List<String> listaPasos,
			@RequestParam("fotosPasos") List<MultipartFile> listaFotoPasos, @RequestParam("foto") MultipartFile foto,
			// @RequestParam(value = "pasoImgs", required = false) List<MultipartFile>
			// pasoImgs,
			@PathVariable("idUsuario") Long idUsuario) {
		log.info("RecetaRestController - add: Añadimos una nueva receta");

		try {
			RecetaDTO recetaDTO = new RecetaDTO();
			recetaDTO.setTitulo(titulo);
			recetaDTO.setDuracion(duracion);
			recetaDTO.setNumPersonas(personas);
			recetaDTO.setExplicacion(presentacion);
			recetaDTO.setPuntuacion(0);
			recetaDTO.setFecha(new Date());

			String extension = FilenameUtils.getExtension(foto.getOriginalFilename());
			String nombreArchivo = "main." + extension;
			recetaDTO.setFoto(nombreArchivo);

			// añadir pais
			PaisDTO pDTO = new PaisDTO();
			pDTO.setId(idPais);
			pDTO = paisService.findById(pDTO);
			recetaDTO.setPaisDTO(pDTO);

			// añadir usuario
			UsuarioDTO uDTO = new UsuarioDTO();
			uDTO.setId(idUsuario);
			uDTO = userService.findById(uDTO);
			recetaDTO.setUsuarioDTO(uDTO);

			recetaDTO = recetaService.save(recetaDTO);
			log.info("RecetaRestController - despues save: receta guardada " + recetaDTO.toString());

			// guardar imagenes en directorio
			try {
				// guardarImagen(foto, recetaDTO.getId(), "main");
				String urlFoto = azureBlobService.subirImagenRecetaJpg(foto, recetaDTO.getId(), "main");
				recetaDTO.setFoto(urlFoto);
				recetaDTO = recetaService.save(recetaDTO);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// añadir ingredientes

			for (String ingredienteStr : listaIngredientes) {
				String[] partes = ingredienteStr.split("\\|");
				if (partes.length != 2)
					continue;

				String idONombre = partes[0];
				String cantidad = partes[1];

				IngredienteDTO ingredienteDTO;

				try {
					Long id = Long.parseLong(idONombre);
					ingredienteDTO = ingredienteService.findById(id);
				} catch (NumberFormatException e) {
					ingredienteDTO = ingredienteService.findByNombre(idONombre);
					if (ingredienteDTO == null) {
						ingredienteDTO = new IngredienteDTO();
						ingredienteDTO.setNombre(idONombre);
						ingredienteDTO.setTipo("Desconocido");
						ingredienteDTO = ingredienteService.save(ingredienteDTO);
					}
				}

				RecetaIngredienteDTO recetaIngredienteDTO = new RecetaIngredienteDTO();
				recetaIngredienteDTO.setRecetaDTO(recetaDTO);
				recetaIngredienteDTO.setIngredienteDTO(ingredienteDTO);
				recetaIngredienteDTO.setCantidad(cantidad);
				log.info("Antes de guardar ingrediente: " + recetaIngredienteDTO);
				ingredienteService.saveRecetaIngrediente(recetaIngredienteDTO);
			}

			// añadir pasos
			int count = 1;
			for (int i = 0; i < listaPasos.size(); i++) {
				String explicacion = listaPasos.get(i);
				String nombreArchivoPaso = "";
				PasoDTO pasoDTO = new PasoDTO();

				if (listaFotoPasos != null && listaFotoPasos.size() > i) {
					MultipartFile fotoPaso = listaFotoPasos.get(i);

					if (fotoPaso != null && !fotoPaso.isEmpty()) {
						String extensionPaso = FilenameUtils.getExtension(fotoPaso.getOriginalFilename());
						String renameArchivo = "f" + count;
						nombreArchivoPaso = renameArchivo + "." + extensionPaso;
						try {
							// guardarImagen(fotoPaso, recetaDTO.getId(), renameArchivo);
							String urlFoto = azureBlobService.subirImagenRecetaJpg(fotoPaso, recetaDTO.getId(),
									renameArchivo);
							pasoDTO.setFoto(urlFoto);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e) {
							log.error("Error inesperado con la imagen principal", e);
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
						}
					}
				}

				pasoDTO.setRecetaDTO(recetaDTO);
				pasoDTO.setNum(count++);
				pasoDTO.setExplicacion(explicacion);
				pasoService.save(pasoDTO);

			}

			return new ResponseEntity<>(recetaDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error al crear receta", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.header("Error-Message", "La imagen no es válida. Por favor, sube una imagen PNG o JPG estándar.")
					.body(null);
		}
	}

	@PutMapping("{idUsuario}/receta/{idReceta}/update")
	public ResponseEntity<RecetaDTO> updateRecipe(@RequestParam("titulo") String titulo,
			@RequestParam("idPais") Long idPais, @RequestParam("duracion") int duracion,
			@RequestParam("personas") int personas, @RequestParam("presentacion") String presentacion,
			@RequestParam("ingredientes") List<String> listaIngredientes,
			@RequestParam("pasos") List<String> listaPasos,
			@RequestParam(value = "fotosPasos", required = false) List<MultipartFile> listaFotoPasos,
			@RequestParam(value = "foto", required = false) MultipartFile foto,
			@PathVariable("idUsuario") Long idUsuario, @PathVariable("idReceta") Long idReceta) {

		log.info("RecetaRestController - update: Actualizamos receta id " + idReceta);
		log.info("RecetaRestController - update: foto " + foto + " listaPasos:" + listaPasos.toString());

		try {
			RecetaDTO recetaDTO = new RecetaDTO();
			recetaDTO.setId(idReceta);
			recetaDTO = recetaService.findById(recetaDTO);

			if (recetaDTO == null) {
				return ResponseEntity.notFound().build();
			}

			recetaDTO.setTitulo(titulo);
			recetaDTO.setDuracion(duracion);
			recetaDTO.setNumPersonas(personas);
			recetaDTO.setExplicacion(presentacion);

			PaisDTO pDTO = new PaisDTO();
			pDTO.setId(idPais);
			recetaDTO.setPaisDTO(paisService.findById(pDTO));

			UsuarioDTO uDTO = new UsuarioDTO();
			uDTO.setId(idUsuario);
			recetaDTO.setUsuarioDTO(userService.findById(uDTO));

			// Si el usuario ha subido una nueva foto principal
			if (foto != null && !foto.isEmpty()) {
				String extension = FilenameUtils.getExtension(foto.getOriginalFilename());
				String nombreArchivo = "main." + extension;
				// recetaDTO.setFoto(nombreArchivo);

				try {
					// guardarImagen(foto, recetaDTO.getId(), "main");
					String urlFoto = azureBlobService.subirImagenRecetaJpg(foto, recetaDTO.getId(), "main");
					recetaDTO.setFoto(urlFoto);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					log.error("Error inesperado con la imagen principal", e);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
				}
			}

			ingredienteService.deleteAllByRecetaId(idReceta);

			for (String ingredienteStr : listaIngredientes) {
				String[] partes = ingredienteStr.split("\\|");
				if (partes.length != 2)
					continue;

				String idONombre = partes[0];
				String cantidad = partes[1];

				IngredienteDTO ingredienteDTO;
				try {
					Long id = Long.parseLong(idONombre);
					ingredienteDTO = ingredienteService.findById(id);
				} catch (NumberFormatException e) {
					ingredienteDTO = ingredienteService.findByNombre(idONombre);
					if (ingredienteDTO == null) {
						ingredienteDTO = new IngredienteDTO();
						ingredienteDTO.setNombre(idONombre);
						ingredienteDTO.setTipo("Desconocido");
						ingredienteDTO = ingredienteService.save(ingredienteDTO);
					}
				}

				RecetaIngredienteDTO recetaIngredienteDTO = new RecetaIngredienteDTO();
				recetaIngredienteDTO.setRecetaDTO(recetaDTO);
				recetaIngredienteDTO.setIngredienteDTO(ingredienteDTO);
				recetaIngredienteDTO.setCantidad(cantidad);

				ingredienteService.saveRecetaIngrediente(recetaIngredienteDTO);
			}

			// --- PASOS
			// Guardar las fotos antiguas de los pasos ANTES de borrarlos
			List<PasoDTO> pasosAntiguos = pasoService.findAllByIdRecipe(recetaDTO);

			// Eliminar pasos antiguos
			pasoService.deleteAllByRecetaId(idReceta);

			// Crear pasos nuevos
			int count = 1;
			for (int i = 0; i < listaPasos.size(); i++) {
				String explicacionPaso = listaPasos.get(i);
				String nombreArchivoPaso = "";
				// Crear paso
				PasoDTO pasoDTO = new PasoDTO();

				if (listaFotoPasos != null && listaFotoPasos.size() > i) {
					MultipartFile fotoPaso = listaFotoPasos.get(i);

					if (fotoPaso != null && !fotoPaso.isEmpty()) {
						// Usuario subió nueva imagen para este paso
						String extensionPaso = FilenameUtils.getExtension(fotoPaso.getOriginalFilename());
						String nombreBase = "f" + count;
						nombreArchivoPaso = nombreBase + "." + extensionPaso;

						try {
							// guardarImagen(fotoPaso, recetaDTO.getId(), nombreBase);
							String urlFoto = azureBlobService.subirImagenRecetaJpg(fotoPaso, recetaDTO.getId(),
									nombreBase);
							pasoDTO.setFoto(urlFoto);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (fotoPaso != null && "DELETE".equals(fotoPaso.getOriginalFilename())) {
						log.info("Paso " + i + ": fotoPaso es delete");
						pasoDTO.setFoto(null);
					} else {
						// Usuario NO subió nueva imagen -> Reutilizar foto antigua si existe
						if (pasosAntiguos != null && pasosAntiguos.size() > i) {
							nombreArchivoPaso = pasosAntiguos.get(i).getFoto();
							pasoDTO.setFoto(nombreArchivoPaso);
						}
					}
				}

				pasoDTO.setRecetaDTO(recetaDTO);
				pasoDTO.setNum(count++);
				pasoDTO.setExplicacion(explicacionPaso);

				pasoService.save(pasoDTO);
			}

			recetaDTO = recetaService.update(recetaDTO);

			return ResponseEntity.ok(recetaDTO);
		} catch (Exception e) {
			log.error("Error al crear receta", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.header("Error-Message", "La imagen no es válida. Por favor, sube una imagen PNG o JPG estándar.")
					.body(null);
		}
	}

	@PostMapping("/api/recetas/{id}/imagenes")
	public ResponseEntity<String> subirImagenes(@PathVariable("id") Long recetaId,
			@RequestParam("mainImg") MultipartFile mainImg,
			@RequestParam(value = "pasoImgs", required = false) List<MultipartFile> pasoImgs) {
		try {
			// Ruta donde se guardarán
			String uploadDir = "uploads/recetas/" + recetaId;

			// Crear carpeta si no existe
			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// Guardar imagen principal
			if (!mainImg.isEmpty()) {
				Path mainPath = Paths.get(uploadDir, "main.jpg");
				mainImg.transferTo(mainPath);
			}

			// Guardar imágenes de pasos
			if (pasoImgs != null) {
				for (int i = 0; i < pasoImgs.size(); i++) {
					MultipartFile pasoImg = pasoImgs.get(i);
					if (!pasoImg.isEmpty()) {
						String fileName = "paso" + (i + 1) + ".jpg";
						Path pasoPath = Paths.get(uploadDir, fileName);
						pasoImg.transferTo(pasoPath);
					}
				}
			}

			return ResponseEntity.ok("Imágenes subidas correctamente");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imágenes");
		}
	}

	@DeleteMapping("{idReceta}/delete")
	public ResponseEntity<String> deleteById(@PathVariable("idReceta") Long idReceta) {
		log.info("RecetaRestController - deleteById: eliminando receta " + idReceta);

		recetaService.deleteRecetaById(idReceta);

		return ResponseEntity.noContent().build();
	}

	List<RecetaDTO> buscarUserYFavoritos(Long idUsuario, List<RecetaDTO> listaRecetasDTO) {
		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);
		List<RecetaDTO> recetasFavoritas = recetaService.findFav(uDTO);
		Set<Long> favIds = recetasFavoritas.stream().map(RecetaDTO::getId).collect(Collectors.toSet());
		listaRecetasDTO.forEach(receta -> receta.setFavorita(favIds.contains(receta.getId())));
		return listaRecetasDTO;
	}

	public void guardarImagen(MultipartFile imagen, Long idReceta, String nombrePersonalizado) throws IOException {
		log.info("Max file size: " + System.getProperty("spring.servlet.multipart.max-file-size"));
		log.info("Max request size: " + System.getProperty("spring.servlet.multipart.max-request-size"));

		String extension = FilenameUtils.getExtension(imagen.getOriginalFilename());
		String nombreArchivo = nombrePersonalizado + "." + extension;

		Path directorioReceta = Paths.get(
				"C:\\Users\\Usuario.DESKTOP-4SUUPMC\\Documents\\DAW\\FACEFOOD\\facefood\\src\\main\\resources\\static\\img\\recetas\\"
						+ idReceta);
		if (!Files.exists(directorioReceta)) {
			Files.createDirectories(directorioReceta); // Crear si no existe
		}
		log.info("path: " + directorioReceta);
		Path rutaArchivo = directorioReceta.resolve(nombreArchivo);
		imagen.transferTo(rutaArchivo.toFile());
	}
}
