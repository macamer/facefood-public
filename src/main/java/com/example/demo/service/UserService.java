package com.example.demo.service;

import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.dto.UsuarioDTO;

public interface UserService {

	UsuarioDTO findByUserAndPass(UsuarioDTO userDTO);

	UsuarioDTO save(UsuarioDTO uDTO);

	UsuarioDTO findByUser(UsuarioDTO usuarioDTO);

	UsuarioDTO findById(UsuarioDTO uDTO);

	void deleteUsuarioById(Long idUsuario);

	String getEmailByResetToken(String token);

	void updatePassword(String email, String newPassword);

	void invalidateResetToken(String token);

	UsuarioDTO findByEmail(String email);

	void saveResetToken(String email, String token);

}
