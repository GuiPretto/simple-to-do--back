package com.teste.projetoteste.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.projetoteste.model.entity.Container;

public interface ContainerRepository extends JpaRepository<Container, Integer> {
	
	boolean existsByTitle(String title);
	
	boolean existsById(int id);

}
