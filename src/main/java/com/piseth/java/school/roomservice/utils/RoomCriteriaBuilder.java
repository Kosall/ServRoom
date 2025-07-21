package com.piseth.java.school.roomservice.utils;

import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

public class RoomCriteriaBuilder {
	public static Criteria build(RoomFilterDTO filter) {
		Criteria criteria=new Criteria();
		if(Objects.nonNull(filter.getName())) {
			///criteria.and("name").is(filter.getName());
			criteria.and("name").is(filter.getName());
		}
		if(Objects.nonNull(filter.getFloor())) {
			criteria.and("attributes.floor").is(filter.getFloor());
			//criteria.and(Roomer.).is(filter.getFloor());
		}
		
	/*	if(Objects.nonNull(filter.getPrice()) && Objects.nonNull(filter.getPriceOperation())) {
			switch(filter.getPriceOperation()) {
			case "lt"-> criteria.and("attribute.price").lt(filter.getPrice());
			case "gt"-> criteria.and("attribute.price").gt(filter.getPrice());
			case "lte"-> criteria.and("attribute.price").lte(filter.getPrice());
			case "gte"-> criteria.and("attribute.price").gte(filter.getPrice());
			case "eq"-> criteria.and("attribute.price").is(filter.getPrice());
			}
		}
		*/
		criteria=getFilterPrice(filter);
		
		return criteria;
		//new Query(criteria).skip((long)filter.getPage()*filter.getSize())
//				.limit(filter.getSize());
		
	}
	
	private static Criteria getFilterPrice(RoomFilterDTO filter) {
		Criteria criteria=new Criteria();
		if(Objects.nonNull(filter.getPrice()) && Objects.nonNull(filter.getPriceOperation())) {
			switch(filter.getPriceOperation()) {
			case "lt"-> criteria.and("attributes.price").lt(filter.getPrice());
			case "gt"-> criteria.and("attributes.price").gt(filter.getPrice());
			case "lte"-> criteria.and("attributes.price").lte(filter.getPrice());
			case "gte"-> criteria.and("attributes.price").gte(filter.getPrice());
			case "eq"-> criteria.and("attributes.price").is(filter.getPrice());
			}
		}else if(Objects.nonNull(filter.getMin()) && Objects.nonNull(filter.getMax())) {
			criteria.and("attributes.price").gte(filter.getMin()).lte(filter.getMax());
		}
		//Query  =new Query(criteria);
		return criteria;
	}
	
	public static Sort sort(RoomFilterDTO dto) {
		Sort.Direction direction=Sort.Direction.ASC;
		if(dto.getDirection().equalsIgnoreCase("desc")) {
			direction=Sort.Direction.DESC;
		}
//		if("desc".equalsIgnoreCase(dto.getDirection())) {
//			direction=Sort.Direction.DESC;
//		}
		String sortBy = dto.getSortBy();
		
		sortBy.equalsIgnoreCase(dto.getName());
		
		if(!sortBy.contains(".")) {
			if(!sortBy.contains("name")) {
				sortBy="attributes."+sortBy;
			}
		}
		return Sort.by(direction,sortBy);
	}

}
