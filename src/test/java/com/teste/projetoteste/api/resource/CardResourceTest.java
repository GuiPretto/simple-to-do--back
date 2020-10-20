package com.teste.projetoteste.api.resource;

import java.util.List;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
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
				.put(API.concat("/update/0"))
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
				.put(API.concat("/update/0"))
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
				.delete(API.concat("/delete/0"))
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
				.delete(API.concat("/delete/0"))
				.accept(JSON)
				.contentType(JSON)
				.content(jsonData);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());	
	}
	
	@Test
	public void shouldGetOneOrMoreCardsFromAContainerId() throws Exception {
		Mockito.when(containerService.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.getAllWithContainerId(Mockito.any(Container.class))).thenReturn(List.of(new Card()));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/getCards/0"));
		
		MvcResult result = mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("[{\"id\":0,\"title\":null,\"container\":null}]");
	}
	
	@Test
	public void shouldGetEmptyListOfCardsFromAContainerId() throws Exception {
		Mockito.when(containerService.getById(Mockito.anyInt())).thenReturn(Optional.of(new Container()));
		Mockito.when(service.getAllWithContainerId(Mockito.any(Container.class))).thenReturn(List.of());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/getCards/0"));
		
		MvcResult result = mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
	}
	
	@Test
	public void shouldGetCard() throws Exception {
		Card card = Card.builder().title("Teste").container(new Container()).build();
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.of(card));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/get/0"));
		
		MvcResult result = mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"id\":0,\"title\":\"Teste\",\"container\":{\"id\":0,\"title\":null}}");
	}
	
	@Test
	public void shouldThrowNotFoundWhenTryingToGetCard() throws Exception {
		Mockito.when(service.getById(Mockito.anyInt())).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API.concat("/get/0"));
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
