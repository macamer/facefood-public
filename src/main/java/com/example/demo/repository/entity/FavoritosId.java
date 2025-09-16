package com.example.demo.repository.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable 
public class FavoritosId implements Serializable {

	private Long id_usu;
	private Long id_receta;
	
	 public FavoritosId() {}
	
	public FavoritosId(Long id_usu, Long id_receta) {
        this.id_usu = id_usu;
        this.id_receta = id_receta;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FavoritosId that = (FavoritosId) o;
		return id_usu == that.id_usu && id_receta == that.id_receta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_usu, id_receta);
	}
}
