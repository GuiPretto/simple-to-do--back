package com.guipretto.simpletodo.service;

import java.util.List;
import java.util.Optional;

import com.guipretto.simpletodo.model.entity.Container;

public interface ContainerService {

		Container createContainer(Container container);
		
		Container updateContainer(Container container);
		
		void deleteContainer(Container container);
				
		void validateNewContainer(Container container);
		
		void validateExistentContainer(Container container);
		
		Optional<Container> getById(int id);
		
		List<Container> getAll();
}
