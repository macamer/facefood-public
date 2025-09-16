package com.example.demo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.ExceptionMessage;

@ControllerAdvice
public class ActionController {
	private static final Logger log = LoggerFactory.getLogger(ActionController.class);
/*
	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CuentaService cuentaService;

	@ExceptionHandler(ExceptionMessage.class)
	public ModelAndView handleMovimientoError(ExceptionMessage ex) {
		log.info("ActionController- errorMessage: " + ex + " movimientoDTO: " + ex.getMovimientoDTO().toString());
		ModelAndView mav = new ModelAndView("movimientoform");

		// Recuperar datos relevantes (si están presentes en la excepción)
		MovimientoDTO movimientoDTO = ex.getMovimientoDTO();
		ClienteDTO clienteDTO = ex.getClienteDTO();

		if (clienteDTO != null) {
			clienteDTO = clienteService.findById(clienteDTO);

			// Agregar cuentas del cliente al modelo
			mav.addObject("listaCuentasDTO", cuentaService.findAllByCliente(clienteDTO));
			mav.addObject("clienteDTO", clienteDTO);
		}

		mav.addObject("movimientoDTO", movimientoDTO != null ? movimientoDTO : new MovimientoDTO());
		mav.addObject("errorMessage", ex.getMessage());

		if (movimientoDTO.getId() == null) {
			mav.addObject("add", true);
		} else {
			mav.addObject("add", false);
		}

		return mav;
	}*/

	@ExceptionHandler(Exception.class)
	public ModelAndView handleUnexpectedError(Exception ex) {
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("errorMessage", "Ha ocurrido un error inesperado." + ex.getMessage());
		return mav;
	}
}
