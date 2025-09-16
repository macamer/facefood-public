package com.example.demo.repository.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data 
@Entity
@Table(name="receta")
public class Receta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private int duracion;
	@Column(name = "num_personas")
	private int numPersonas;
	private String explicacion;
	private float puntuacion;
	private String tipo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;
	private String foto;
	private int visualizaciones;
	
	@ManyToOne
	@JoinColumn(name="id_pais")
	private Pais pais;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RecetaIngrediente> listaRecetaIngredientes = new ArrayList<>();

	@OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Paso> listaPasos = new ArrayList<>();
    
}
