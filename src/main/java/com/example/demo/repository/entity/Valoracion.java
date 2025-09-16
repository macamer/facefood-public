package com.example.demo.repository.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data 
@Entity
@Table(name="valoracion")
public class Valoracion {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private float calificacion;
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name="id_receta")
	private Receta receta;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	

}
