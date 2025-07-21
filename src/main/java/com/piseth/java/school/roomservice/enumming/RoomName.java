package com.piseth.java.school.roomservice.enumming;

public enum RoomName {
	NAME("name"),
	FLOOR("floor"),
	TYPE("type");
	private final String description;
	private RoomName(String description) {
	this.description=description;
		
	}
	
	public String getDescription() {
		return this.description;
	}

}
