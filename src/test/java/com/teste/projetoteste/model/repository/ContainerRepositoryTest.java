package com.teste.projetoteste.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.teste.projetoteste.model.entity.Container;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ContainerRepositoryTest {
	
	@Autowired
	ContainerRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void shouldCreateNewContainer() {
		Container container = newContainer();
		
		container = repository.save(container);
		
		Assertions.assertThat(container.getId()).isNotNull();
	}
	
	@Test
	public void shouldDeleteContainer() {
		Container container = newContainer();
		entityManager.persist(container);
		container = entityManager.find(Container.class, container.getId());
		
		repository.delete(container);
		
		Container deletedContainer = entityManager.find(Container.class, container.getId());
		Assertions.assertThat(deletedContainer).isNull();
	}
	
	@Test
	public void shouldUpdateCard() {
		Container container = newContainer();
		entityManager.persist(container);
		container.setTitle("Teste2");
				
		repository.save(container);
		
		Container updatedContainer = entityManager.find(Container.class, container.getId());
		Assertions.assertThat(updatedContainer.getTitle()).isEqualTo("Teste2");
	}
	
	@Test
	public void shouldFindCardById() {
		Container container = newContainer();
		entityManager.persist(container);
		
		Optional<Container> foundContainer = repository.findById(container.getId());

		Assertions.assertThat(foundContainer.isPresent()).isTrue();
	}
	
	@Test
	public void shouldCheckForExistentTitle() {
		Container container = newContainer();

		entityManager.persist(container);
		
		boolean result = repository.existsByTitle("Teste");
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void shouldCheckForNonExistentTitle() {
		boolean result  = repository.existsByTitle("Teste");
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void shouldCheckForExistentId() {
		Container container = newContainer();

		entityManager.persist(container);
				
		boolean result = repository.existsById(container.getId());
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void shouldCheckForNonExistentId() {
		boolean result  = repository.existsById(1);
		
		Assertions.assertThat(result).isFalse();
	}

	private Container newContainer() {
		return Container.builder().title("Teste").build();
	}
}
