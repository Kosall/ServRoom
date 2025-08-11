package com.piseth.java.school.roomservice.domain;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
@Document(collation = "skipped_room")
public class SkippedRoomDocument {
	@Id
	private String id;
	private int rowNumber;
	private Map<String, Object>rowData;
	private String reasons;
	private LocalDateTime uploadTime;
	private String uploadBatchId;
}
