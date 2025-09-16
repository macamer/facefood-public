package com.example.demo.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.repository.entity.Pais;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface PaisRepository extends JpaRepository<Pais, Long> {

	
}
