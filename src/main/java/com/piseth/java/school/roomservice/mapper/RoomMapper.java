package com.piseth.java.school.roomservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;

@Mapper(componentModel = "spring")
public interface RoomMapper {
	
	Room toRoom(RoomDTO roomDTO);
	
	RoomDTO toRoomDTO(Room room);
	
	void updateRoomFromDTO(RoomDTO roomDTO,@MappingTarget Room entity);
	
}
