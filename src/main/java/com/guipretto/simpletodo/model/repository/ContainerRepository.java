package com.guipretto.simpletodo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guipretto.simpletodo.model.entity.Container;

public interface ContainerRepository extends JpaRepository<Container, Integer> {
	
	boolean existsByTitle(String title);
	
	boolean existsById(int id);

}
