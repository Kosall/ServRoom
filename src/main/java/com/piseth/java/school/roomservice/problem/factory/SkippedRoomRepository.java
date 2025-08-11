package com.piseth.java.school.roomservice.problem.factory;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piseth.java.school.roomservice.domain.SkippedRoomDocument;

public interface SkippedRoomRepository extends ReactiveMongoRepository<SkippedRoomDocument,String> {

}
