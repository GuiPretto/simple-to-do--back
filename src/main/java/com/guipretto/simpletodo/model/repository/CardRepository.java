package com.guipretto.simpletodo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guipretto.simpletodo.model.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	boolean existsByTitle(String title);
	
	boolean existsById(int id);

}
