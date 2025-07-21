package com.piseth.java.school.roomservice.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.exception.RoomNotFoundException;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;
import com.piseth.java.school.roomservice.utils.RoomCriteriaBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

//	@Autowired
	private final RoomRepository roomRepository;
//	@Autowired
	private final RoomMapper roomMapper;

	private final RoomCustomRepository customRepository;

	@Override
	public Mono<RoomDTO> createRoom(RoomDTO roomDTO) {
		log.debug("Saving room to DB: {}", roomDTO);

		Room room = roomMapper.toRoom(roomDTO);

		return roomRepository.save(room).doOnSuccess(saved -> log.info("Room saved: {}", saved))
				.map(roomMapper::toRoomDTO);

	}

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.debug("Retreiving room with ID: {}", id);
		return roomRepository.findById(id).switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnNext(room -> log.info("Room received : {}", room)).map(roomMapper::toRoomDTO);

	}

	@Override
	public Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO) {
		log.debug("Updating romm id: {} with data : {}", id, roomDTO);

		return roomRepository.findById(id).switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.flatMap(existing -> {

					roomMapper.updateRoomFromDTO(roomDTO, existing);
					Mono<Room> monoRoom = roomRepository.save(existing);
					return monoRoom;
				}).map(roomMapper::toRoomDTO);

	}

	@Override
	public Mono<Void> deleteRoom(String id) {
		log.debug("deleting romm with id: {}", id);
		return roomRepository.deleteById(id).switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnSuccess(deleted -> log.info("Room with id ({})", id + " :DELETED"));

	}

	@Override
	public Flux<RoomDTO> findRoomByName(String name) {
		// TODO Auto-generated method stub
		return roomRepository.researchRoom(name).map(roomMapper::toRoomDTO);
	}

	@Override
	public Flux<RoomDTO> findByFilter(RoomFilterDTO filterDTO) {
		Criteria query = RoomCriteriaBuilder.build(filterDTO);
		return customRepository.findByFilter(new Query(query)).map(roomMapper::toRoomDTO);

	}

	@Override
	public Mono<PageDTO<RoomDTO>> findByPagination(RoomFilterDTO filterDTO) {
		Criteria criteria = RoomCriteriaBuilder.build(filterDTO);
		Mono<Long> count = customRepository.countByFilter(new Query(criteria));
		Query query=new Query(criteria)
				.skip((long)filterDTO.getPage()* filterDTO.getSize())
				.limit(filterDTO.getSize());
		query.with((RoomCriteriaBuilder.sort(filterDTO)));
		Flux<RoomDTO> room = customRepository.findByFilter(query).map(roomMapper::toRoomDTO);

		return Mono.zip(room.collectList(), count).map(tuple -> {

			List<RoomDTO> roome = tuple.getT1();
			Long total = tuple.getT2();
			int totalPage = (int) (Math.ceil((double) total / filterDTO.getSize()));
			return new PageDTO<>(filterDTO.getPage(), filterDTO.getSize(), total, totalPage, roome);
		});

	}

}
