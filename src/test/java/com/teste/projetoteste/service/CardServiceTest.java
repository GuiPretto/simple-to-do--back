package com.teste.projetoteste.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.teste.projetoteste.exception.CardException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.model.repository.CardRepository;
import com.teste.projetoteste.service.implementation.CardServiceImplementation;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CardServiceTest  {
	
	@MockBean
	CardRepository repository;

	@SpyBean
	CardServiceImplementation service;

	@Test
	public void shouldCreateNewAvailableCard() {
		Card card = newCard();
		Mockito.doNothing().when(service).validateNewCard(Mockito.any(Card.class));
		Mockito.when(repository.save(Mockito.any(Card.class))).thenReturn(card);

		Card savedCard = service.createCard(card);
	
		Assert.assertEquals(card.getTitle(), savedCard.getTitle());
	}
	
	@Test
	public void shouldThrowErrorWhenTryingToCreateNotAvailableCard() {
		Card card = newCard();
		Mockito.doThrow(CardException.class).when(service).validateNewCard(Mockito.any(Card.class));

		CardException exception = assertThrows(CardException.class, () -> service.createCard(card));
		
		Assertions.assertThat(exception).isInstanceOf(CardException.class);	
		Mockito.verify(repository, Mockito.never()).save(card);
	}
	
	@Test
	public void shouldUpdateAlreadyCreatedCard() {
		Card card = newCard();
		Mockito.doNothing().when(service).validateExistentCard(Mockito.any(Card.class));
		Mockito.when(repository.save(Mockito.any(Card.class))).thenReturn(card);
		
		service.updateCard(card);
	
		Mockito.verify(repository, Mockito.times(1)).save(card);
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToUpdateNotCreatedCard() {
		Card card = newCard();
		
		CardException exception = assertThrows(CardException.class, () -> service.updateCard(card));

		Assertions.assertThat(exception).isInstanceOf(CardException.class);
		Mockito.verify(repository, Mockito.never()).save(card);
	}
	
	@Test
	public void shouldDeleteAlreadyCreatedCard() {
		Card card = newCard();
		Mockito.doNothing().when(service).validateExistentCard(Mockito.any(Card.class));
		
		service.deleteCard(card);
	
		Mockito.verify(repository, Mockito.times(1)).delete(card);
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToDeleteNotCreatedCard() {
		Card card = newCard();
		
		CardException exception = assertThrows(CardException.class, () -> service.deleteCard(card));

		Assertions.assertThat(exception).isInstanceOf(CardException.class);
		Mockito.verify(repository, Mockito.never()).delete(card);
	}
	
	@Test
	public void shouldValidateNewCardWithNotUsedTitle() {
		Card card = newCard();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(false);
		
		service.validateNewCard(card);
	}
	 
	@Test
	public void shouldThrowErrorWhenValidatingNewCardWithAlreadyUsedTitle() {
		Card card = newCard();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(true);
				
		CardException exception = assertThrows(CardException.class, () -> service.validateNewCard(card));
		
		Assertions.assertThat(exception).isInstanceOf(CardException.class);
	}
	
	@Test
	public void shouldGetListOfCardsContainingAtLeasOneCard() {
		Card card = newCard();
		List<Card> cardList = Arrays.asList(card);
		Example<Card> example;
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(cardList);
		
		List<Card> newCardList = service.getAllWithContainerId(new Container());
		
		Assertions.assertThat(newCardList).isNotEmpty();
	}
	
	@Test
	public void shouldGetListOfCardsEmpty() {
		Card card = newCard();
		List<Card> cardList = Arrays.asList();
		Example<Card> example;
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(cardList);
		
		List<Card> newCardList = service.getAllWithContainerId(new Container());
		
		Assertions.assertThat(newCardList).isEmpty();
	}
	
	@Test
	public void shouldValidateExistentCardWithNotUsedTitle() {
		Card card = newCard();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(true);
		
		service.validateExistentCard(card);
	}
	 
	@Test
	public void shouldThrowErrorWhenValidatingExistentCardWithAlreadyUsedTitle() {
		Card card = newCard();
		Mockito.when(repository.existsByTitle(Mockito.anyString())).thenReturn(false);
				
		CardException exception = assertThrows(CardException.class, () -> service.validateExistentCard(card));
		
		Assertions.assertThat(exception).isInstanceOf(CardException.class);
	}
	
	@Test
	public void shouldGetCardById() {
		Card card = newCard();
		Optional<Card> optCard;
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		
		optCard = service.getById(1);
		
		Assertions.assertThat(optCard).isPresent();
	}
	
	@Test
	public void shouldReceiveEmptyOptionalWhenTryingToGetCardById() {
		Optional<Card> optCard;
		
		optCard = service.getById(1);
		
		Assertions.assertThat(optCard).isNotPresent();
	}
	
	private Card newCard() {
		Container container = Container.builder().title("Container").build();
		Card card = Card.builder().title("Teste").container(container).build();
		return card;
	}
}
