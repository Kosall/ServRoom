package com.piseth.java.school.roomservice.repository.impl;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@Repository
public class RoomCustomRepositoryImpl implements RoomCustomRepository {
	private final ReactiveMongoTemplate reactiveMongoTemplate;

	@Override
	public Flux<Room> findByFilter(Query query) {
		// TODO Auto-generated method stub
		return reactiveMongoTemplate.find(query, Room.class);
	}

	@Override
	public Mono<Long> countByFilter(Query query) {
		// TODO Auto-generated method stub
		return reactiveMongoTemplate.count(query, Room.class);
	}
	

}
