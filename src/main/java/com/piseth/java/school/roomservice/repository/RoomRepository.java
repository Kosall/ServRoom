package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;

import reactor.core.publisher.Flux;
@Repository
public interface RoomRepository extends ReactiveMongoRepository<Room, String>{
	@Query("{'name':?0}")
	Flux<Room> researchRoom(String name);
}
