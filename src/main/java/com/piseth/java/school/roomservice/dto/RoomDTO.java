package com.piseth.java.school.roomservice.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomDTO {
	//API level validation
	@NotBlank(message="Room name is required!")
	@Size(max = 30,message = "room name must be at most 30 characters!")
	private String name;
	private Map<String, Object> attributes;
	

}
