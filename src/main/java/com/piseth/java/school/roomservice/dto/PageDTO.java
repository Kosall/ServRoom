package com.piseth.java.school.roomservice.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Schema(description = "pagination response wrapper")
public class PageDTO<T> {
	@Schema(description = "Current page nunber(0-based-page)")
	private int page;
	@Schema(description = "Nunber of record per a page")
	private int size;
	@Schema(description = "Total nunber of records")
	private long totalElement;
	@Schema(description = "Total nunber of pages")
	private int totalPage;
	@Schema(description = "Current page data list")
	private List<T> content;

}
