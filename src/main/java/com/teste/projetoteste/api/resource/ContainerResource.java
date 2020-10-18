package com.teste.projetoteste.api.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.projetoteste.api.dto.ContainerDTO;
import com.teste.projetoteste.exception.ContainerException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.service.CardService;
import com.teste.projetoteste.service.ContainerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
public class ContainerResource {
	
	private final ContainerService service;
	private final CardService cardService;
	
	@PostMapping("/create")
	public ResponseEntity create(@RequestBody ContainerDTO dto) {
		try {
			Container container = convert(dto);
			container = service.createContainer(container);
			return new ResponseEntity(container, HttpStatus.CREATED);
		} catch (ContainerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id") int id, @RequestBody ContainerDTO dto) {
		return service.getById(id).map( e -> {
			try {
				Container container = convert(dto);
				container.setId(e.getId());
				container = service.updateContainer(container);
				return ResponseEntity.ok(container);
			} catch (ContainerException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Container não encontrado na base da dados.", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		return service.getById(id).map( e -> {
			try {
				List<Card> cardList = cardService.getAllWithContainerId(e);
				cardList.forEach((card) -> {
					cardService.deleteCard(card);
				});
				service.deleteContainer(e);					
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} catch (ContainerException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Container não encontrado na base da dados.", HttpStatus.BAD_REQUEST));
	}
	
	private Container convert(ContainerDTO dto) {
		return Container.builder().title(dto.getTitle()).build();
	}
}
