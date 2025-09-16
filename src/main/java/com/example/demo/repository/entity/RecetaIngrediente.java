package com.example.demo.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "lista_ingredientes")
public class RecetaIngrediente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_receta")
	private Receta receta;

	@ManyToOne
	@JoinColumn(name = "id_ingrediente")
	private Ingrediente ingrediente;

	@Column(name = "cantidad")
	private String cantidad;

}
