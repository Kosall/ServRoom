package com.piseth.java.school.roomservice.utils;

import java.util.List;

public class RoomConstants {
	//Let someone to not create new
	private RoomConstants() {
		
	}
	public static final String ATTR="attributes.";
	public static final String FIELD_NAME="name";
	public static final String FIELD_FLOOR=ATTR+"floor";
	public static final String FIELD_PRICE=ATTR+"price";
	
	public static final String OP_LT="lt";
	public static final String OP_LTE="lte";
	public static final String OP_GT="gt";
	public static final String OP_GTE="gte";
	public static final String OP_EQ="eq";
	
	public static final List<String>ALLOWED_SORT_FIELD=List.of("name","price","floor");

}
