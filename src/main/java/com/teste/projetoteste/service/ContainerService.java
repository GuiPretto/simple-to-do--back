package com.teste.projetoteste.service;

import java.util.Optional;

import com.teste.projetoteste.model.entity.Container;

public interface ContainerService {

		Container createContainer(Container container);
		
		Container updateContainer(Container container);
		
		void deleteContainer(Container container);
		
		void validateNewContainer(Container container);
		
		void validateExistentContainer(Container container);
		
		Optional<Container> getById(int id);
}
