package com.piseth.java.school.roomservice.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.domain.SkippedRoomDocument;
import com.piseth.java.school.roomservice.dto.RoomImportSummary;
import com.piseth.java.school.roomservice.problem.factory.SkippedRoomRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomImportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomImportServiceImpl implements RoomImportService{
	private final SkippedRoomRepository repository;
	private final RoomRepository  repositoryRoom;

	@Override
	public Mono<RoomImportSummary> importRoom(FilePart filePart) {
		return 	filePart.content()
		.map(dataBuffer->{
			byte [] bytes=new byte[dataBuffer.readableByteCount()];
			dataBuffer.read(bytes);
			DataBufferUtils.release(dataBuffer);
			return new ByteArrayInputStream(bytes);
		}).next()
		.flatMap(inputStream->parseAndSaveRoom(inputStream));
		
	}
	
	
	private Mono<RoomImportSummary> parseAndSaveRoom(ByteArrayInputStream inputStream) {
		String batchId =UUID.randomUUID().toString();
		try (Workbook book=new XSSFWorkbook(inputStream)){
			Sheet sheet= book.getSheetAt(0);
			List<Room> validRoom=new ArrayList<>();
			List<Integer>skippedRow=new ArrayList<>();
			Map<Integer,String> reasons=new HashedMap<>();
			List<SkippedRoomDocument>skippedRoomDocument=new ArrayList<>();
			for(int i=1;i<=sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				int displayRow=i+1;
				if(row==null) {
					skippedRow.add(displayRow);
					reasons.put(displayRow, "Empty row");
					skippedRoomDocument.add(buildSkippedRoomDocument(displayRow, Collections.emptyMap(), "Empty row", batchId));
					continue;
				}
				
				String name=getString(row.getCell(0));
				Double price=getDouble(row.getCell(1));
				Double floorValue=getDouble(row.getCell(2));
				int floor= (floorValue==null)? 0 :floorValue.intValue();
				String type=getString(row.getCell(3));
				Map<String, Object> rowData=new HashMap<>();
				rowData.put("name", name);
				rowData.put("price", price);
				rowData.put("floor", floor);
				rowData.put("type", type);
				String reason=null;
				if(!StringUtils.hasText(name)) {
					reason="Missing or invalid field name";
				}else if(price==null) {
					reason="Missing or invalid field price";
				}else if(floor==0) {
					reason="Missing or invalid field floor";
				}else if(type==null){
					reason="Missing or invalid field type";
				}
				if(reason!=null) {
					
						skippedRow.add(displayRow);
						reasons.put(displayRow, reason);
						skippedRoomDocument.add(buildSkippedRoomDocument(displayRow, rowData, reason, batchId));
						continue;
					
				}
				Room room =new Room();
				room.setName(name);
				room.setAttributes(rowData);
				log.debug("preparing to save room {}",room);
				validRoom.add(room);
			}
			log.info("valid room to save {}",validRoom.size());
		return	repository.saveAll(skippedRoomDocument).thenMany(repositoryRoom.saveAll(validRoom)
					.doOnNext(r->log.info("Saving room {}",r))
					.doOnError(err->log.error("error saving room {}",err.getMessage()))
					).collectList()
					.map(saved->new RoomImportSummary(saved.size(),skippedRoomDocument.size(),skippedRow,reasons));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Fail to parse excel {}",e);
			return Mono.error(new RuntimeException());
		}
		
	}
	
	private SkippedRoomDocument buildSkippedRoomDocument(
			int rowNumber,
			Map<String,
			Object>rowData,
			String reason,
			String batchId) {
		
		return 	SkippedRoomDocument.builder()
		.rowNumber(rowNumber)
		.rowData(rowData)
		.reasons(reason)
		.uploadBatchId(batchId)
		.uploadTime(LocalDateTime.now())
		.build();
		
		
	}
	private String getString(Cell cell) {
		return cell==null? null : cell.getStringCellValue();
	}
	private Double getDouble(Cell cell) {
		
		try {return cell==null? null : cell.getNumericCellValue();
		}
		catch(Exception e){
			return null;
		}
		
	}

}
