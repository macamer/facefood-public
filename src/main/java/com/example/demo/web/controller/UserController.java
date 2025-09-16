package com.example.demo.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.dto.PaisDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.service.PaisService;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	@Autowired
	PaisService paisService;

	//redirect to login.html
	@GetMapping("/login")
	public ModelAndView login() {
		log.info("LoginController- login: mostramos vista login");

		ModelAndView mav = new ModelAndView("login");
		mav.addObject("userDTO", new UsuarioDTO());

		return mav;
	}
	
	//redirect to register.html
	@GetMapping("/add")
	public ModelAndView add() {
		log.info("LoginController- add: mostramos vista register");
		
		//mostrar paises
		List<PaisDTO> listCountriesDTO = paisService.findAll();

		ModelAndView mav = new ModelAndView("userform");
		mav.addObject("userDTO", new UsuarioDTO());
		mav.addObject("listCountriesDTO", listCountriesDTO);
		mav.addObject("add", true);
		
		return mav;
	}

	@GetMapping("/{usuario}/myaccount")
	public ModelAndView myAccount() {
		log.info("LoginController- myaccount: mostramos vista de mi cuenta");

		ModelAndView mav = new ModelAndView("userform");
		
		return mav;
	}
	
	@GetMapping("/reset-password")
	public ModelAndView resetPassword() {
		log.info("UserController- resetpassword: mostramos vista de resetpassword");

		ModelAndView mav = new ModelAndView("reset-password");
		
		return mav;
	}
	/*
	@PostMapping("/")
	public ModelAndView verifyUser(@ModelAttribute("userDTO") UsuarioDTO userDTO, RedirectAttributes redirectAttributes) {
		log.info("LoginController- verifyUser: vemos si existe usuario: " + userDTO.toString());

		ModelAndView mav;

		UsuarioDTO uDTO = new UsuarioDTO();

		try {
			uDTO = userService.findByUserAndPass(userDTO);
			log.info("LoginController- verifyUser: usuario encontrado: " + uDTO.toString());
			mav = new ModelAndView("main");
			mav.addObject("userDTO", uDTO);
		} catch (ExceptionMessage ex) {
			mav = new ModelAndView("redirect:/login");
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			redirectAttributes.addFlashAttribute("userDTO", userDTO);
		}

		return mav;
	}
	*/
	
}
