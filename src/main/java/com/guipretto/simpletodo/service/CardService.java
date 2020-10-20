package com.guipretto.simpletodo.service;

import java.util.List;
import java.util.Optional;

import com.guipretto.simpletodo.model.entity.Card;
import com.guipretto.simpletodo.model.entity.Container;

public interface CardService {
	
	Card createCard(Card card);
	
	Card updateCard(Card card);
	
	void deleteCard(Card card);
	
	List<Card> getAllWithContainerId(Container containerFilter);
	
	void validateNewCard(Card card);
	
	void validateExistentCard(Card card);
	
	Optional<Card> getById(int id);
}
