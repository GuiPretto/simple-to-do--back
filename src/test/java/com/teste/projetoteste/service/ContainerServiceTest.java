package com.teste.projetoteste.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guipretto.simpletodo.exception.ContainerException;
import com.guipretto.simpletodo.model.entity.Container;
import com.guipretto.simpletodo.model.repository.ContainerRepository;
import com.guipretto.simpletodo.service.implementation.ContainerServiceImplementation;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ContainerServiceTest {

	@MockBean
	ContainerRepository repository;

	@SpyBean
	ContainerServiceImplementation service;

	@Test
	public void shouldCreateNewAvailableContainer() {
		Container container = newContainer();
		Mockito.doNothing().when(service).validateNewContainer(Mockito.any(Container.class));
		Mockito.when(repository.save(Mockito.any(Container.class))).thenReturn(container);

		Container savedContainer = service.createContainer(container);
	
		Assert.assertEquals(container.getTitle(), savedContainer.getTitle());
	}
	
	@Test
	public void shouldThrowErrorWhenTryingToCreateNotAvailableContainer() {
		Container container = newContainer();
		Mockito.doThrow(ContainerException.class).when(service).validateNewContainer(Mockito.any(Container.class));

		ContainerException exception = assertThrows(ContainerException.class, () -> service.createContainer(container));
		
		Assertions.assertThat(exception).isInstanceOf(ContainerException.class);	
		Mockito.verify(repository, Mockito.never()).save(container);
	}
	
	@Test
	public void shouldUpdateAlreadyCreatedContainer() {
		Container container = newContainer();
		Mockito.doNothing().when(service).validateExistentContainer(Mockito.any(Container.class));
		Mockito.when(repository.save(Mockito.any(Container.class))).thenReturn(container);
		
		service.updateContainer(container);
	
		Mockito.verify(repository, Mockito.times(1)).save(container);
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToUpdateNotCreatedContainer() {
		Container container = newContainer();
		
		ContainerException exception = assertThrows(ContainerException.class, () -> service.updateContainer(container));

		Assertions.assertThat(exception).isInstanceOf(ContainerException.class);
		Mockito.verify(repository, Mockito.never()).save(container);
	}
	
	@Test
	public void shouldDeleteAlreadyCreatedContainer() {
		Container container = newContainer();
		Mockito.doNothing().when(service).validateExistentContainer(Mockito.any(Container.class));
		
		service.deleteContainer(container);
	
		Mockito.verify(repository, Mockito.times(1)).delete(container);
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToDeleteNotCreatedContainer() {
		Container container = newContainer();
		
		ContainerException exception = assertThrows(ContainerException.class, () -> service.deleteContainer(container));

		Assertions.assertThat(exception).isInstanceOf(ContainerException.class);
		Mockito.verify(repository, Mockito.never()).delete(container);
	}
	
	@Test
	public void shouldValidateNewContainerWithNotUsedTitle() {
		Container container = newContainer();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(false);
		
		service.validateNewContainer(container);
	}
	 
	@Test
	public void shouldThrowErrorWhenValidatingNewContainerWithAlreadyUsedTitle() {
		Container container = newContainer();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(true);
				
		ContainerException exception = assertThrows(ContainerException.class, () -> service.validateNewContainer(container));
		
		Assertions.assertThat(exception).isInstanceOf(ContainerException.class);
	}
	
	@Test
	public void shouldValidateExistentContainerWithNotUsedTitle() {
		Container container = newContainer();
		Mockito.when(repository.existsById(Mockito.anyInt())).thenReturn(true);
		
		service.validateExistentContainer(container);
	}
	 
	@Test
	public void shouldThrowErrorWhenValidatingExistentContainerWithAlreadyUsedTitle() {
		Container container = newContainer();
		Mockito.when(repository.existsById(Mockito.anyInt())).thenReturn(false);
				
		ContainerException exception = assertThrows(ContainerException.class, () -> service.validateExistentContainer(container));
		
		Assertions.assertThat(exception).isInstanceOf(ContainerException.class);
	}
	
	@Test
	public void shouldGetContainerById() {
		Container container = newContainer();
		Optional<Container> optContainer;
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(container));
		
		optContainer = service.getById(1);
		
		Assertions.assertThat(optContainer).isPresent();
	}
	
	@Test
	public void shouldReceiveEmptyOptionalWhenTryingToGetContainerById() {
		Optional<Container> optContainer;
		
		optContainer = service.getById(1);
		
		Assertions.assertThat(optContainer).isNotPresent();
	}
	
	@Test
	public void shouldGetAllContainers() {
		Mockito.when(repository.findAll()).thenReturn(List.of(new Container()));
		
		List<Container> containerList = service.getAll();
		
		Assertions.assertThat(containerList.size()).isEqualByComparingTo(1);
	}
	
	@Test
	public void shouldReturnEmptyListWhenTryingToGetAllContainers() {
		Mockito.when(repository.findAll()).thenReturn(List.of());
		
		List<Container> containerList = service.getAll();
		
		Assertions.assertThat(containerList).isEmpty();
	}
	
	private Container newContainer() {
		return Container.builder().title("Teste").build();
	}
}
