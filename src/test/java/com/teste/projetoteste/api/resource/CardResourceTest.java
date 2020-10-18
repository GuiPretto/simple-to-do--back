package com.teste.projetoteste.api.resource;

import java.util.Optional;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.projetoteste.api.dto.CardDTO;
import com.teste.projetoteste.exception.CardException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.service.CardService;
import com.teste.projetoteste.service.ContainerService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CardResource.class)
@AutoConfigureMockMvc
public class CardResourceTest {

	static final String API = "/api/cards";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	CardService service;
	
	@MockBean
	ContainerService containerService;
	
	@Test
	public void shouldCreateACard() throws Exception {
		Container container = Container.builder().title("Teste").build();
		Mockito.when(containerService.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Card card = Card.builder().title("Teste").build();
		Mockito.when(service.createCard(Mockito.any(Card.class))).thenReturn(card);
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/create"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("title").value(card.getTitle()));	
	}
	
	@Test
	public void shouldReceiveABadRequestAfterTryingToCreateACard() throws Exception {
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		Mockito.when(service.createCard(Mockito.any(Card.class))).thenThrow(CardException.class);
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
	public void shouldUpdateACard() throws Exception {
		Container container = Container.builder().title("Teste").build();
		Card card = Card.builder().title("Teste").container(container).build();
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		Mockito.when(containerService.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Card()));
		Mockito.when(service.updateCard(Mockito.any(Card.class))).thenReturn(card);
		String jsonData = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(API.concat("/update/2"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("title").value(card.getTitle()));	
	}
	
	@Test
	public void shouldReceiveABadRequestAfterTryingToUpdateACard() throws Exception {
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		Mockito.when(containerService.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Card()));
		Mockito.when(service.updateCard(Mockito.any(Card.class))).thenThrow(CardException.class);
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
	public void shouldDeleteACard() throws Exception {
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Card()));
		Mockito.doNothing().when(service).deleteCard(Mockito.any(Card.class));
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
		CardDTO dto = CardDTO.builder().title("Teste").idContainer(0).build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(new Card()));
		Mockito.doThrow(CardException.class).when(service).deleteCard(Mockito.any(Card.class));
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
}
