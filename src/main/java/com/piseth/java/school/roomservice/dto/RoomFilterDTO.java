package com.piseth.java.school.roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilterDTO {
	@Schema(description = "filter by floor number",example = "Floor 3")
	private Integer floor;
	private String name;
	private String type;
	private Double price;
	private  Double min;
	private Double max;
	private String priceOperation;
	private int size=10;
	private int page=0;

	@Schema(description = "Sort by field name (ex: name, floor)", example = "name")
	private String sortBy;
	
	@Schema(description = "Sort direction: asc or desc", example = "asc")
	private String direction;

}
