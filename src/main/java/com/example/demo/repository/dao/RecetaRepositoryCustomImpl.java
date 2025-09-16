package com.example.demo.repository.dao;

import java.util.List;

import com.example.demo.repository.entity.Receta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class RecetaRepositoryCustomImpl implements RecetaRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Receta> findByWords(String texto) {
	    String[] palabras = texto.split("\\s+");
	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id WHERE "
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("(");
	        consulta.append("r.titulo LIKE ?").append((i * 4) + 1).append(" OR ");
	        consulta.append("i.nombre LIKE ?").append((i * 4) + 2).append(" OR ");
	        consulta.append("u.nombre LIKE ?").append((i * 4) + 3).append(" OR ");
	        consulta.append("u.usuario LIKE ?").append((i * 4) + 4).append(")");
	    }

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    for (int i = 0; i < palabras.length; i++) {
	        for (int j = 0; j < 4; j++) {
	            query.setParameter((i * 4) + (j + 1), "%" + palabras[i] + "%");
	        }
	    }

	    return query.getResultList();
	}

	
	@Override
	public List<Receta> findBestRecetasByWordsAndCountry(Long idPais, String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE r.id_pais = :idPais AND ("
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("(");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	        consulta.append(")");
	    }

	    consulta.append(") ORDER BY r.puntuacion DESC");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    query.setParameter("idPais", idPais);
	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findNewRecetasByWordsAndCountry(Long idPais, String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE r.id_pais = :idPais AND ("
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("(");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	        consulta.append(")");
	    }

	    consulta.append(") ORDER BY r.fecha DESC");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    query.setParameter("idPais", idPais);
	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findOldRecetasByWordsAndCountry(Long idPais, String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE r.id_pais = :idPais AND ("
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("(");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	        consulta.append(")");
	    }

	    consulta.append(") ORDER BY r.fecha");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    query.setParameter("idPais", idPais);
	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findByWordsAndCountry(Long idPais, String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE r.id_pais = :idPais AND ("
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("(");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	        consulta.append(")");
	    }

	    consulta.append(") ");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    query.setParameter("idPais", idPais);
	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findBestRecetasByWords(String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE "
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	    }

	    consulta.append(" ORDER BY r.puntuacion DESC");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findNewRecetasByWords(String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE "
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	    }

	    consulta.append(" ORDER BY r.fecha DESC");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}
	
	@Override
	public List<Receta> findOldRecetasByWords(String texto) {
	    String[] palabras = texto.split("\\s+");

	    StringBuilder consulta = new StringBuilder(
	        "SELECT DISTINCT r.* FROM receta r " +
	        "LEFT JOIN lista_ingredientes l ON r.id = l.id_receta " +
	        "LEFT JOIN ingrediente i ON l.id_ingrediente = i.id " +
	        "JOIN usuario u ON r.id_usuario = u.id " +
	        "WHERE "
	    );

	    for (int i = 0; i < palabras.length; i++) {
	        if (i > 0) consulta.append(" OR ");
	        consulta.append("r.titulo LIKE :palabra").append(i).append(" ");
	        consulta.append("OR i.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.nombre LIKE :palabra").append(i).append(" ");
	        consulta.append("OR u.usuario LIKE :palabra").append(i).append(" ");
	    }

	    consulta.append(" ORDER BY r.fecha");

	    Query query = entityManager.createNativeQuery(consulta.toString(), Receta.class);

	    for (int i = 0; i < palabras.length; i++) {
	        query.setParameter("palabra" + i, "%" + palabras[i] + "%");
	    }

	    return query.getResultList();
	}

}
