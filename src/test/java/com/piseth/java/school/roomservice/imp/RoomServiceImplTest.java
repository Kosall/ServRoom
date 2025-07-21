package com.piseth.java.school.roomservice.imp;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;
import com.piseth.java.school.roomservice.service.impl.RoomServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)

public class RoomServiceImplTest {
	@Mock
	private RoomRepository repository;
	@Mock
	private RoomMapper mapper;
	@Mock
	private RoomCustomRepository customRepository;
	@InjectMocks
	private RoomServiceImpl roomService;
	@Test
	 void testRoomSuccessfullPart() {
		 //given
		RoomDTO room =new RoomDTO();
		room.setName("R001");
		Room roomDto=new Room();
		roomDto.setName(room.getName());
		//when
		when(mapper.toRoom(room)).thenReturn(roomDto);
		when(repository.save(roomDto)).thenReturn(Mono.just(roomDto));
		when(mapper.toRoomDTO(roomDto)).thenReturn(room);
		//then
		StepVerifier.create(roomService.createRoom(room))
		.expectNext(room)
		.verifyComplete();
		
		
	}
	

}
