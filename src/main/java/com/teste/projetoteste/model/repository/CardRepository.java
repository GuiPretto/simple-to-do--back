package com.teste.projetoteste.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.projetoteste.model.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	boolean existsByTitle(String title);
	
	boolean existsById(int id);

}
