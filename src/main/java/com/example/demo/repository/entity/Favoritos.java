package com.example.demo.repository.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data 
@Entity
//@IdClass(FavoritosId.class)
@Table(name="favoritos")
public class Favoritos {

	@EmbeddedId
	private FavoritosId id; // Clave primaria compuesta
	/*
    @Id
    private Long id_usu;

    @Id
    private Long id_receta;
    */

    @ManyToOne
    @JoinColumn(name = "id_usu", insertable = false, updatable = false)  // Relación con Usuario
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_receta", insertable = false, updatable = false)  // Relación con Receta
    private Receta receta;
    
    public Favoritos() {}
    
    public Favoritos(FavoritosId id) {
        this.id = id;
    }

}
