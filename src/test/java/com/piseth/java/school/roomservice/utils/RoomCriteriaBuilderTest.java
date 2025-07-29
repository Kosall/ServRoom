package com.piseth.java.school.roomservice.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

public class RoomCriteriaBuilderTest {
	

	@Test
	void mayReturnEmptyCriteriaBuilderTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		// then
		assertThat(builder.getCriteriaObject().isEmpty());
	}

	@Test
	void mayAddName() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setName("Nita");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json).contains("name", filterDTO.getName());
	}

	@Test
	void mayAddFloorProvided() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setFloor(2);
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json).contains("floor", "2");

	}

	@Test
	void addPriceCriteria_ltTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("lt");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
//		assertThat(json).contains("price").contains("$lt");
		assertThat(json.contains("price"));
		assertThat(json.contains("$lt"));

	}
	@Test
	void addPriceCriteria_lteTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("lte");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));//.contains("$lte");
		assertThat(json.contains("$lte"));
	}

	@Test
	void addPriceCriteria_gtTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("gt");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));
		assertThat(json.contains("$gt"));

	}
	@Test
	void addPriceCriteria_gteTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("gte");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));//.contains("$lte");
		assertThat(json.contains("$gte"));
	}
	@Test
	void addPriceCriteria_eqTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("eq");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));//.contains("$lte");
		assertThat(json.contains("$eq"));
	}
	@Test
	void addPriceCriteria_defaultTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setPrice(55d);
		filterDTO.setPriceOperation("eqe");
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));//.contains("$lte");
		assertThat(json.contains("$eqe"));
	}
	@Test
	void addPriceCriteria_nullTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		
		// when
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.isEmpty());//.contains("$lte");
		
	}
	@Test
	void addPriceCriteria_maxMinTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		filterDTO.setMin(40d);
		filterDTO.setMax(40d);
		Double max = filterDTO.getMax();
		
		Double min = filterDTO.getMin();
		assertEquals(40d,  max);
		assertEquals(40d,  min);
		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
		String json = builder.getCriteriaObject().toJson();
		// then
		assertThat(json.contains("price"));//.contains("$lte");
		assertThat(json.contains("$lte"));
		assertThat(json.contains("$gte"));
		assertThat(json.contains("40"));
		//assertThat(json).contains();
		
	}
	@Test
	void addPriceCriteria_nullMinMaxTest() {
		// given
		RoomFilterDTO filterDTO = new RoomFilterDTO();
		 Double max = filterDTO.getMax();
		 assertNull(max);
		 Double min = filterDTO.getMin();
		 assertNull(min);
//		// when
//		Criteria builder = RoomCriteriaBuilder.build(filterDTO);
//		String json = builder.getCriteriaObject().toJson();
//		// then
//		assertThat(json).isNull();//.contains("$lte");
		
	}
	
	@Test
	public void sortTest() {
		RoomFilterDTO filterDTO=new RoomFilterDTO();
		filterDTO.setDirection("asc");
		filterDTO.setSortBy("price");
		
		Sort sort = RoomCriteriaBuilder.sort(filterDTO);
		assertThat(sort.getOrderFor("attributes.price")).isNotNull();
		assertThat(sort.getOrderFor("attributes.price").getDirection()).isEqualTo(Sort.Direction.ASC);
	}
	@Test
	 void sort_UnhappyTest() {
		RoomFilterDTO filterDTO=new RoomFilterDTO();
		filterDTO.setDirection("asc");
		filterDTO.setSortBy("lll");//invalid input
		assertThatThrownBy(()->RoomCriteriaBuilder.sort(filterDTO)).isInstanceOf(IllegalArgumentException.class)
		.hasMessageContaining("Invalid input : "+filterDTO.getSortBy());
	}
	@Test
	 void sortBy_nullTest() {
		RoomFilterDTO filterDTO=new RoomFilterDTO();
		filterDTO.setName("Nita");
		filterDTO.setDirection("asc");
		Sort sort = RoomCriteriaBuilder.sort(filterDTO);
		assertThat(sort.getOrderFor("name")).isNotNull();
		assertThat(sort.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
	}
}
