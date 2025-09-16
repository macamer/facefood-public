package com.example.demo.repository.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "ingrediente")
public class Ingrediente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String tipo;

	/*
	 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy =
	 * "direccion")
	 * 
	 * @ToString.Exclude private Set<ClienteDireccion> listaClientesDirecciones;
	 */
}
