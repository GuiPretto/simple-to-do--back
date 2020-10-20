package com.teste.projetoteste.model.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guipretto.simpletodo.model.entity.Card;
import com.guipretto.simpletodo.model.entity.Container;
import com.guipretto.simpletodo.model.repository.CardRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CardRepositoryTeste {
	
	@Autowired
	CardRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void shouldCreateNewCard() {
		Card card = newCard();

		card = repository.save(card);
		
		Assertions.assertThat(card.getId()).isNotNull();
	}
	
	@Test
	public void shouldDeleteCard() {
		Card card = newCard();
		entityManager.persist(card);
		
		card = entityManager.find(Card.class, card.getId());
		
		repository.delete(card);
		
		Card deletedCard = entityManager.find(Card.class, card.getId());
		Assertions.assertThat(deletedCard).isNull();
	}
	
	@Test
	public void shouldUpdateCard() {
		Card card = newCard();
		entityManager.persist(card);
		card.setTitle("Teste2");
				
		repository.save(card);
		
		Card updatedCard = entityManager.find(Card.class, card.getId());
		Assertions.assertThat(updatedCard.getTitle()).isEqualTo("Teste2");
	}
	
	@Test
	public void shouldFindAllCardsByContainer() {
		Container container = Container.builder().title("Container").build();
		entityManager.persist(container);
		Card card = newCard();
		card.setContainer(container);
		entityManager.persist(card);
		Card altCard = newCard();
		altCard.setTitle("Teste2");
		altCard.setContainer(container);
		entityManager.persist(altCard);
		
		Example<Card> example = Example.of(card, ExampleMatcher.matching().withIgnorePaths("id","title"));
		List<Card> carList = repository.findAll(example);

		Assertions.assertThat(carList.size()).isEqualTo(2);
	}
	
	@Test
	public void shouldFindCardById() {
		Card card = newCard();
		entityManager.persist(card);
		
		Optional<Card> foundCard = repository.findById(card.getId());

		Assertions.assertThat(foundCard.isPresent()).isTrue();
	}
	
	@Test
	public void shouldCheckForExistentTitle() {
		Container container = Container.builder().title("Container Teste").build();
		entityManager.persist(container);
		Card card = Card.builder().title("Card Teste").container(container).build();
		entityManager.persist(card);
		
		boolean result = repository.existsByTitle("Card Teste");
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void shouldCheckForNonExistentTitle() {
		boolean result  = repository.existsByTitle("Teste");
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void shouldCheckForExistentId() {
		Container container = Container.builder().title("Container Teste").build();
		entityManager.persist(container);
		Card card = Card.builder().title("Card Teste").container(container).build();
		entityManager.persist(card);
		
		boolean result = repository.existsById(5);
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void shouldCheckForNonExistentId() {
		boolean result  = repository.existsById(1);
		
		Assertions.assertThat(result).isFalse();
	}
	
	private Card newCard() {
		Container container = Container.builder().title("Container").build();
		Card card = Card.builder().title("Teste").container(container).build();
		return card;
	}

}
