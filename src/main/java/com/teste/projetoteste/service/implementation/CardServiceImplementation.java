package com.teste.projetoteste.service.implementation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teste.projetoteste.exception.CardException;
import com.teste.projetoteste.exception.ContainerException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.model.repository.CardRepository;
import com.teste.projetoteste.service.CardService;

@Service
public class CardServiceImplementation implements CardService {

	private CardRepository repository;
	
	public CardServiceImplementation(CardRepository repository) {
		super();
		this.repository = repository;
	}
	 
	@Override
	@Transactional
	public Card createCard(Card card) {
		validateNewCard(card);
		return repository.save(card);
	}

	@Override
	@Transactional
	public Card updateCard(Card card) {
		Objects.requireNonNull(card.getId());
		validateExistentCard(card);
		return repository.save(card);
	}

	@Override
	@Transactional
	public void deleteCard(Card card) {
		Objects.requireNonNull(card.getId());
		validateExistentCard(card);
		repository.delete(card);
	}

	@Override
	public void validateNewCard(Card card) {
		if (card.getTitle() == null || card.getTitle().trim().equals("")) {
			throw new CardException("Digite um nome válido.");
		}
		if (card.getContainer() == null) {
			throw new CardException("Não existe um Container associado à este card");
		}
		boolean exists = repository.existsByTitle(card.getTitle());
		if (exists) {
			throw new CardException("Já existe um Card com este nome.");
		}	
	}

	@Override
	public void validateExistentCard(Card card) {
		if (card.getTitle() == null || card.getTitle().trim().equals("")) {
			throw new CardException("Card com título inválido.");
		}
		boolean exists = repository.existsByTitle(card.getTitle());
		if (!exists) {
			throw new CardException("Este Card não existe.");
		}
	}

	@Override
	public Optional<Card> getById(int id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Card> getAllWithContainerId(Container containerFilter) {
		Card cardFilter = Card.builder().container(containerFilter).build();
		Example<Card> example = Example.of(cardFilter, ExampleMatcher.matching().withIgnorePaths("id"));
		return repository.findAll(example);
	}
}
