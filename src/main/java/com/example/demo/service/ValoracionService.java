package com.example.demo.service;

import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.ValoracionDTO;

public interface ValoracionService {

	ValoracionDTO findByRecetaUsuario(RecetaDTO rDTO, UsuarioDTO uDTO);

}
