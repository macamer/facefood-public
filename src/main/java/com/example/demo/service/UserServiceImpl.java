package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.repository.dao.PasswordResetTokenRepository;
import com.example.demo.repository.dao.UserRepository;
import com.example.demo.repository.entity.PasswordResetToken;
import com.example.demo.repository.entity.Receta;
import com.example.demo.repository.entity.Usuario;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UsuarioDTO findByUserAndPass(UsuarioDTO userDTO) {
		log.info("UserService - findByUserAndPass: ver si existe user " + userDTO.toString());

		Optional<Usuario> uOp = userRepository.findByUsuario(userDTO.getUsuario());

		if (uOp.isPresent()) {
			Usuario usuario = uOp.get();
			if (usuario != null && passwordEncoder.matches(userDTO.getContra(), usuario.getContra())) {
				return UsuarioDTO.convertToDTO(usuario);
			}
		}

		return null;
	}

	@Override
	public UsuarioDTO save(UsuarioDTO uDTO) {
	    log.info("UserServiceImpl - save: salvamos el usuario: " + uDTO);

	    Usuario usuario;
	    if (uDTO.getId() != null) { // Es edición
	        usuario = userRepository.findById(uDTO.getId()).orElse(null);
	        if (usuario == null) throw new RuntimeException("Usuario no encontrado para editar");
	        // Actualiza SOLO los campos editables
	        usuario.setNombre(uDTO.getNombre());
	        usuario.setApellidos(uDTO.getApellidos());
	        usuario.setEmail(uDTO.getEmail());
	        usuario.setUsuario(uDTO.getUsuario());
	        usuario.setAvatar(uDTO.getAvatar());
	        usuario.setPresentacion(uDTO.getPresentacion());
	        usuario.setPais(PaisDTO.convertToEntity(uDTO.getPaisDTO()));
	        if (uDTO.getContra() != null && !uDTO.getContra().isBlank()) {
	            usuario.setContra(uDTO.getContra()); // Ya debería venir hasheada del controller
	        }
	        // ¡NO toques recetas!
	    } else { // Es alta
	        usuario = UsuarioDTO.convertToEntity(uDTO, PaisDTO.convertToEntity(uDTO.getPaisDTO()));
	    }
	    userRepository.save(usuario);
	    return UsuarioDTO.convertToDTO(usuario);
	}


	@Override
	public UsuarioDTO findByUser(UsuarioDTO uDTO) {
		log.info("UserServiceImpl - save: salvamos el  usuario: " + uDTO.getUsuario());

		Optional<Usuario> user = userRepository.findByUser(uDTO.getUsuario());

		if (user.isPresent()) {
			UsuarioDTO userDTO = UsuarioDTO.convertToDTO(user.get());
			return userDTO;
		} else {
//			throw new ExceptionMessage("El usuario " + userDTO.getUsuario() + " no existe.");
			return null;
		}
	}

	@Override
	public UsuarioDTO findById(UsuarioDTO userDTO) {
		log.info("UserService - findById: buscar usuario por id " + userDTO.toString());

		Optional<Usuario> user = userRepository.findById(userDTO.getId());

		if (user.isPresent()) {
			UsuarioDTO uDTO = UsuarioDTO.convertToDTO(user.get());
			return uDTO;
		} else {
			// throw new ExceptionMessage("El usuario " + userDTO.getUsuario() + " no
			// existe.");
			return null;
		}
	}

	@Override
	public UsuarioDTO findByEmail(String email) {
		log.info("UserService - findByEmail: buscar usuario por email " + email);

		Optional<Usuario> user = userRepository.findByEmail(email);

		if (user.isPresent()) {
			UsuarioDTO uDTO = UsuarioDTO.convertToDTO(user.get());
			return uDTO;
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public void deleteUsuarioById(Long idUsuario) {
		log.info("UserService - findDeleteById: eliminar usuario por id " + idUsuario);
		if (!userRepository.existsById(idUsuario)) {
			throw new RuntimeException("Receta no encontrada");
		}
		userRepository.deleteById(idUsuario);

	}

	@Override
	public String getEmailByResetToken(String token) {
		Optional<PasswordResetToken> tokenRecord = passwordResetTokenRepository.findByToken(token);

		if (tokenRecord.isPresent()) {
			PasswordResetToken resetToken = tokenRecord.get();

			if (resetToken.getExpiration().isAfter(LocalDateTime.now())) {
				return resetToken.getEmail();
			}
		}
		return null;
	}

	@Override
	public void updatePassword(String email, String newPassword) {
		Optional<Usuario> usuarioOp = userRepository.findByEmail(email);
		Usuario usuario = new Usuario();
		if (usuarioOp.isPresent()) {
			usuario = usuarioOp.get();
			usuario.setContra(passwordEncoder.encode(newPassword));
			userRepository.save(usuario);
		}

	}

	public void saveResetToken(String email, String token) {
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setEmail(email);
		resetToken.setToken(token);
		resetToken.setExpiration(LocalDateTime.now().plusMinutes(15)); // Token expira en 15 min
		passwordResetTokenRepository.save(resetToken);
	}

	public void invalidateResetToken(String token) {
		passwordResetTokenRepository.deleteByToken(token);
	}

}
