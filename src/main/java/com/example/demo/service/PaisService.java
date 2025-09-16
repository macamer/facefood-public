package com.example.demo.service;

import java.util.List;
import com.example.demo.model.dto.PaisDTO;


public interface PaisService {

	List<PaisDTO> findAll();

	PaisDTO findById(PaisDTO p);

}
