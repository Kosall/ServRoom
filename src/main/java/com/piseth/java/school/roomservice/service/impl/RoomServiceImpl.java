package com.piseth.java.school.roomservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService{
	
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private RoomMapper roomMapper;
	
	@Override
	public Mono<RoomDTO> createRoom(RoomDTO roomDTO) {
		log.debug("Saving room to DB: {}", roomDTO);
		
		Room room = roomMapper.toRoom(roomDTO);
		
		return roomRepository.save(room)
			.doOnSuccess(saved -> log.info("Room saved: {}",saved))
			.map(roomMapper::toRoomDTO);
		
	}

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.debug("Retreiving room with ID: {}", id);
		return roomRepository.findById(id)
				.doOnNext(room -> log.info("Room received : {}", room))
				.map(roomMapper::toRoomDTO);
				
	}

	@Override
	public Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO) {
		log.debug("Updating romm id: {} with data : {}", id, roomDTO);
		
		  return roomRepository.findById(id)
			.flatMap(existing ->{
				roomMapper.updateRoomFromDTO(roomDTO, existing);
				Mono<Room> monoRoom = roomRepository.save(existing);
				return monoRoom;
			})
			.map(roomMapper::toRoomDTO);
		
	}

}
