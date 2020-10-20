package com.teste.projetoteste.service.implementation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teste.projetoteste.exception.ContainerException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.model.repository.ContainerRepository;
import com.teste.projetoteste.service.ContainerService;

@Service
public class ContainerServiceImplementation implements ContainerService {

	private ContainerRepository repository;
	
	public ContainerServiceImplementation(ContainerRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public Container createContainer(Container container) {
		validateNewContainer(container);
		return repository.save(container);
	}
	
	@Override
	@Transactional
	public Container updateContainer(Container container) {
		Objects.requireNonNull(container.getId());
		validateExistentContainer(container);
		return repository.save(container);
	}

	@Override
	@Transactional
	public void deleteContainer(Container container) {
		Objects.requireNonNull(container.getId());
		validateExistentContainer(container);
		repository.delete(container);
	}
	
	@Override
	public void validateNewContainer(Container container) {
		if (container.getTitle() == null || container.getTitle().trim().equals("")) {
			throw new ContainerException("Digite um nome válido.");
		}
		boolean exists = repository.existsByTitle(container.getTitle());
		if (exists) {
			throw new ContainerException("Já existe um Grupo com este nome.");
		}		
	}

	@Override
	public void validateExistentContainer(Container container) {
		if (container.getTitle() == null || container.getTitle().trim().equals("")) {
			throw new ContainerException("Container com título inválido.");
		}
		boolean exists = repository.existsById(container.getId());
		if (!exists) {
			throw new ContainerException("Este container não existe.");
		}
	}

	@Override
	@Transactional
	public List<Container> getAll() {
		return repository.findAll();
	}		
	
	@Override
	public Optional<Container> getById(int id) {
		return repository.findById(id);
	}

}
