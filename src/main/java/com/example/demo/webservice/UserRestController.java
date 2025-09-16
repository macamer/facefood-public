package com.example.demo.webservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.service.AzureBlobService;
import com.example.demo.service.EmailService;
import com.example.demo.service.PaisService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
public class UserRestController {

	private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PaisService paisService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AzureBlobService azureBlobService;

	// Alta de usuarios
	@PostMapping
	public ResponseEntity<UsuarioDTO> add(
			@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam("usuario") String usuario,
            @RequestParam("pais") Long pais,
            @RequestParam("contra") String contra,
            @RequestParam(value = "presentacion", required = false) String presentacion,
            @RequestParam(value = "foto", required = false) MultipartFile foto
            ) {
		log.info("UserRestController - add: Añadimos un nuevo cliente, pais: " + pais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(pais);
		pDTO = paisService.findById(pDTO);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setNombre(nombre);
		uDTO.setApellidos(apellidos);
		uDTO.setEmail(email);
		uDTO.setUsuario(usuario);
		uDTO.setPaisDTO(pDTO);
		uDTO.setContra(passwordEncoder.encode(contra));
		uDTO.setPresentacion(presentacion);
		
		String extension = FilenameUtils.getExtension(foto.getOriginalFilename());
		String nombreArchivo = usuario + "." + extension;
						
		//guardar avatar
		try {
			//guardarImagen(foto, nombreArchivo);
			String urlFoto = azureBlobService.subirAvatarUsuarioJpg(foto, usuario);
			uDTO.setAvatar(urlFoto);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uDTO = userService.save(uDTO);
		
		UsuarioDTO loggedUser = new UsuarioDTO();
		loggedUser.setUsuario(usuario);
		loggedUser.setContra(contra);
		uDTO = userService.findByUserAndPass(loggedUser);
		
		if (uDTO == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(uDTO, HttpStatus.OK);
		}
	}

	@GetMapping("/{userName}")
	public ResponseEntity<UsuarioDTO> findByUserName(@PathVariable("userName") String userName) {
		log.info("UserRestController - findByUser: verificamos si existe el usuario:" + userName);

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsuario(userName);
		usuarioDTO = userService.findByUser(usuarioDTO);

		if (usuarioDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{email}/email")
	public ResponseEntity<UsuarioDTO> findByEmail(@PathVariable("email") String email) {
		log.info("UserRestController - findByEmail: verificamos si existe el email:" + email);

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO = userService.findByEmail(email);

		if (usuarioDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
		}
	}

	@PostMapping("/{userName}/{password}")
	public ResponseEntity<UsuarioDTO> verifyUser(@PathVariable("userName") String userName,
			@PathVariable("password") String password) {
		log.info("UserRestController - verifyUser: verificamos si existe el usuario:" + userName);

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsuario(userName);
		usuarioDTO.setContra(password);
		usuarioDTO = userService.findByUserAndPass(usuarioDTO);

		if (usuarioDTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/{idUsuario}/delete")
	public ResponseEntity<String> deleteById(@PathVariable("idUsuario") Long idUsuario) {
		log.info("RecetaRestController - deleteById: eliminando usuario " + idUsuario);

		userService.deleteUsuarioById(idUsuario);

		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("{idUsuario}/update")
	public ResponseEntity<UsuarioDTO> updateById(
			@PathVariable("idUsuario") Long idUsuario,
			@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam("usuario") String usuario,
            @RequestParam("pais") Long pais,
            @RequestParam("contra") String contra,
            @RequestParam(value = "presentacion", required = false) String presentacion,
            @RequestParam(value = "foto", required = false) MultipartFile foto
            ) {
		log.info("UserRestController - update: Actualizamos el cliente "+idUsuario+", pais: " + pais);

		PaisDTO pDTO = new PaisDTO();
		pDTO.setId(pais);
		pDTO = paisService.findById(pDTO);

		UsuarioDTO uDTO = new UsuarioDTO();
		uDTO.setId(idUsuario);
		uDTO = userService.findById(uDTO);

		if (uDTO == null) {
			return ResponseEntity.notFound().build();
		}

		uDTO.setNombre(nombre);
		uDTO.setApellidos(apellidos);
		uDTO.setEmail(email);
		uDTO.setUsuario(usuario);
		uDTO.setPaisDTO(pDTO);
		if (contra != null && !contra.isBlank()) {			
			uDTO.setContra(passwordEncoder.encode(contra));
		}
		uDTO.setPresentacion(presentacion);
		
		if (foto != null && !foto.isEmpty()) {
			String extension = FilenameUtils.getExtension(foto.getOriginalFilename());
			String nombreArchivo = usuario + "." + extension;
			//uDTO.setAvatar(nombreArchivo);

			try {
				//guardarImagen(foto, usuario);
				String urlFoto = azureBlobService.subirAvatarUsuarioJpg(foto, usuario);
				uDTO.setAvatar(urlFoto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		uDTO = userService.save(uDTO);
		
		if (uDTO == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(uDTO, HttpStatus.OK);
		}
	}
	
	@PostMapping("/requestPassword")
	public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    UsuarioDTO usuario = userService.findByEmail(email);

	    if (usuario != null) {
	        String token = UUID.randomUUID().toString();
	        userService.saveResetToken(email, token);

	        String resetLink = "https://facefood-fafqasddcydqf6ah.spaincentral-01.azurewebsites.net/facefood/reset-password?token=" + token;
	        emailService.sendPasswordResetEmail(email, resetLink); 
	    }

	    // Siempre respondemos igual para seguridad
	    return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace de recuperación.");
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
		log.info("UserRestController - resetPassword: Actualizamos la contraseña "+body);
	    String token = body.get("token");
	    String newPassword = body.get("newPassword");

	    String email = userService.getEmailByResetToken(token);
	    if (email == null) {
	        return new ResponseEntity<>("Token inválido o expirado", HttpStatus.BAD_REQUEST);
	    }

	    userService.updatePassword(email, newPassword); // Hashea con BCrypt
	    userService.invalidateResetToken(token);

	    log.info("Contraseña cambiada con éxito.");
	    return ResponseEntity.ok("Contraseña cambiada con éxito.");
	}

	
	public void guardarImagen(MultipartFile imagen, String nombrePersonalizado) throws IOException {
		log.info("Max file size: " + System.getProperty("spring.servlet.multipart.max-file-size"));
		log.info("Max request size: " + System.getProperty("spring.servlet.multipart.max-request-size"));

		String extension = FilenameUtils.getExtension(imagen.getOriginalFilename());
		String nombreArchivo = nombrePersonalizado + "." + extension;

		Path directorioUsuario = Paths.get("C:\\Users\\Usuario.DESKTOP-4SUUPMC\\Documents\\DAW\\FACEFOOD\\facefood\\src\\main\\resources\\static\\img\\avatar\\");
		if (!Files.exists(directorioUsuario)) {
			Files.createDirectories(directorioUsuario); // Crear si no existe
		}
		log.info("path: " + directorioUsuario);
		Path rutaArchivo = directorioUsuario.resolve(nombreArchivo);
		log.info("Guardando imagen en: " + rutaArchivo);
		imagen.transferTo(rutaArchivo.toFile());
	}
}
