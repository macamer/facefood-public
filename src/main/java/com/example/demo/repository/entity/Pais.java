package com.example.demo.repository.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data 
@Entity
@Table(name="pais")
public class Pais {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="pais")
	@ToString.Exclude
	private Set<Usuario> listaUsuarios;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="pais")
	@ToString.Exclude
	private Set<Receta> listaRecetas;
	
	public Pais() {
		super();
		this.listaUsuarios = new HashSet<Usuario>();
		this.listaRecetas = new HashSet<Receta>();
	}
	
}
