package com.teste.projetoteste.api.resource;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guipretto.simpletodo.api.dto.ContainerDTO;
import com.guipretto.simpletodo.api.resource.ContainerResource;
import com.guipretto.simpletodo.exception.ContainerException;
import com.guipretto.simpletodo.model.entity.Container;
import com.guipretto.simpletodo.service.CardService;
import com.guipretto.simpletodo.service.ContainerService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ContainerResource.class)
@AutoConfigureMockMvc
public class ContainerResourceTest {

	static final String API = "/api/containers";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ContainerService service;
	
	@MockBean
	CardService cardService;
	
	@Test
	public void shouldCreateAContainer() throws Exception {
		Container container = Container.builder().title("Teste").build();
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.createContainer(Mockito.any(Container.class))).thenReturn(container);
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/create"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("title").value(container.getTitle()));	
	}
	
	@Test
	public void shouldReceiveABadRequestAfterTryingToCreateAContainer() throws Exception {
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.createContainer(Mockito.any(Container.class))).thenThrow(ContainerException.class);
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/create"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shouldUpdateAContainer() throws Exception {
		Container container = Container.builder().title("Teste").build();
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.updateContainer(Mockito.any(Container.class))).thenReturn(container);
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(API.concat("/update/2"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("title").value(container.getTitle()));	
	}
	
	@Test
	public void shouldReceiveABadRequestAfterTryingToUpdateAContainer() throws Exception {
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.updateContainer(Mockito.any(Container.class))).thenThrow(ContainerException.class);
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(API.concat("/update/2"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shouldDeleteAContainer() throws Exception {
		Container container = Container.builder().title("Teste").build();
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(container));
		Mockito.doNothing().when(service).deleteContainer(Mockito.any(Container.class));
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(API.concat("/delete/2"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());	
	}
	
	@Test
	public void shouldReceiveABadRequestAfterTryingToDeleteAContainer() throws Exception {
		Container container = Container.builder().title("Teste").build();
		ContainerDTO dto = ContainerDTO.builder().title("Teste").build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(container));
		Mockito.doThrow(ContainerException.class).when(service).deleteContainer(Mockito.any(Container.class));
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(API.concat("/delete/2"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());	
	}

	@Test
	public void shouldGetContainer() throws Exception {
		Container container = Container.builder().title("Teste").build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(container));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/get/0"));
		
		MvcResult result = mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"id\":0,\"title\":\"Teste\"}");
	}
	
	@Test
	public void shouldThrowNotFoundWhenTryingToGetContainer() throws Exception {
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/get/0"));
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
