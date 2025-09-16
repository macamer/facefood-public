package com.example.demo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.dto.RecetaDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.service.UserService;

@Controller
public class RecetaController {
	
	private static final Logger log = LoggerFactory.getLogger(RecetaController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{userName}")
	public ModelAndView main(@PathVariable("userName") String userName) {
		log.info("RecetaController - index: Mostramos el main, usuario: " + userName);
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsuario(userName);
		usuarioDTO = userService.findByUser(usuarioDTO);
		
		//Buscar lista de recetas
		
		ModelAndView mav = new ModelAndView("main");
		mav.addObject("userDTO", usuarioDTO);
		return mav;
	}
	
	@GetMapping("/receta/{idRecipe}")
	public ModelAndView recipeById(@PathVariable("idRecipe") Long idRecipe) {
		log.info("RecetaController - recipe: Mostramos la receta " + idRecipe);
		
		//Buscar lista de recetas
		ModelAndView mav = new ModelAndView("recipe");
		return mav;
	}

	@GetMapping("/{userName}/receta/{idRecipe}")
	public ModelAndView recipe(@PathVariable("userName") String userName,
			@PathVariable("idRecipe") Long idRecipe) {
		log.info("RecetaController - recipe: Mostramos el recipe, usuario: " + userName + " y receta " + idRecipe);
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsuario(userName);
		usuarioDTO = userService.findByUser(usuarioDTO);
		
		//Buscar lista de recetas
		ModelAndView mav = new ModelAndView("recipe");
		mav.addObject("userDTO", usuarioDTO);
		return mav;
	}
	
	@GetMapping("/{userName}/receta/add")
	public ModelAndView addRecipe(@PathVariable("userName") String userName) {
		log.info("RecetaController - recipe: Mostramos el recipeform, usuario: " + userName );
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsuario(userName);
		usuarioDTO = userService.findByUser(usuarioDTO);
		
		ModelAndView mav = new ModelAndView("recipeform");
		mav.addObject("userDTO", usuarioDTO);
		return mav;
	}
	
	@GetMapping("/{userName}/receta/{id}/update")
	public ModelAndView editRecipe(@PathVariable("userName") String userName, @PathVariable("id") Long id) {
	    log.info("RecetaController - recipe: Editamos receta id: " + id + ", usuario: " + userName);

	    ModelAndView mav = new ModelAndView("recipeform");
	    return mav;
	}

}
