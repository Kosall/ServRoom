package com.piseth.java.school.roomservice.utils;

import static com.piseth.java.school.roomservice.utils.RoomConstants.ALLOWED_SORT_FIELD;
import static com.piseth.java.school.roomservice.utils.RoomConstants.ATTR;
import static com.piseth.java.school.roomservice.utils.RoomConstants.FIELD_FLOOR;
import static com.piseth.java.school.roomservice.utils.RoomConstants.FIELD_NAME;
import static com.piseth.java.school.roomservice.utils.RoomConstants.FIELD_PRICE;
import static com.piseth.java.school.roomservice.utils.RoomConstants.OP_EQ;
import static com.piseth.java.school.roomservice.utils.RoomConstants.OP_GT;
import static com.piseth.java.school.roomservice.utils.RoomConstants.OP_GTE;
import static com.piseth.java.school.roomservice.utils.RoomConstants.OP_LT;
import static com.piseth.java.school.roomservice.utils.RoomConstants.OP_LTE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class RoomCriteriaBuilder {
	private RoomCriteriaBuilder() {
		
	}
	public static Criteria build(RoomFilterDTO filter) {
		
		getFilterPrice(filter);
		List<Criteria> criterias=new ArrayList<>();
		
		if(Objects.nonNull(filter.getName())) {
			///criteria.and("name").is(filter.getName());
//			criteria.and(FIELD_NAME).is(filter.getName());
			criterias.add(Criteria.where(FIELD_NAME).is(filter.getName()));
		}
		if(Objects.nonNull(filter.getFloor())) {
//			criteria.and(FIELD_FLOOR).is(filter.getFloor());
			//criteria.and(Roomer.).is(filter.getFloor());
			criterias.add(Criteria.where(FIELD_FLOOR).is(filter.getFloor()));
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
		
		
		
		return criterias.isEmpty()? new Criteria(): new Criteria().andOperator(criterias.toArray(Criteria[]::new));
		//new Query(criteria).skip((long)filter.getPage()*filter.getSize())
//				.limit(filter.getSize());
		
	}
	
	private static Criteria getFilterPrice(RoomFilterDTO filter) {

		List<Criteria> criterias=new ArrayList<>();
		if(Objects.nonNull(filter.getPrice()) && Objects.nonNull(filter.getPriceOperation())) {
			switch(filter.getPriceOperation()) {
			case OP_LT-> criterias.add(Criteria.where(FIELD_PRICE).lt(filter.getPrice()));
			case OP_GT-> criterias.add(Criteria.where(FIELD_PRICE).gt(filter.getPrice()));
			case OP_LTE-> criterias.add(Criteria.where(FIELD_PRICE).lte(filter.getPrice()));
			case OP_GTE-> criterias.add(Criteria.where(FIELD_PRICE).gte(filter.getPrice()));
			case OP_EQ-> criterias.add(Criteria.where(FIELD_PRICE).is(filter.getPrice()));
			default ->log.warn("Invalid input operator {}",filter.getPriceOperation());
			}
		}else if(Objects.nonNull(filter.getMin()) && Objects.nonNull(filter.getMax())) {
			criterias.add(Criteria.where(FIELD_PRICE).gte(filter.getMin()).lte(filter.getMax()));
		}
		//Query  =new Query(criteria);
		return criterias.isEmpty()? new Criteria(): new Criteria().andOperator(criterias.toArray(Criteria[]::new));
	}
	
	public static Sort sort(RoomFilterDTO dto) {
		Sort.Direction direction="asc".equalsIgnoreCase(dto.getDirection())? Sort.Direction.ASC:Sort.Direction.DESC;
//		if(dto.getDirection().equalsIgnoreCase("desc")) {
//			direction=Sort.Direction.DESC;
//		}
//	if("desc".equalsIgnoreCase(dto.getDirection())) {
//			direction=Sort.Direction.DESC;		}
//	
		String sortBy =Objects.nonNull(dto.getSortBy())?  dto.getSortBy():FIELD_NAME ;
		if(!ALLOWED_SORT_FIELD.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid input : "+sortBy);
		}
		
		if(!sortBy.contains(".")) {
			if(!sortBy.contains(FIELD_NAME)) {
				sortBy=ATTR+sortBy;
			}
		}
		return Sort.by(direction,sortBy);
	}

}
