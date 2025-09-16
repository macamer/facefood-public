package com.example.demo.repository.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data 
@Entity
@Table(name="usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	private String nombre;
	@Column(name="ape")
	private String apellidos;
	private String email;
	@Column(name="contra")
	private String contra;
	private String usuario;
	private String avatar;
	private String presentacion;
	
	@ManyToOne
	@JoinColumn(name="id_pais")
	private Pais pais;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Receta> recetas;
	
}
