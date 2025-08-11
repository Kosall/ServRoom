package com.piseth.java.school.roomservice.controller;

import org.springframework.boot.io.ApplicationResourceLoader.FilePathResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.dto.RoomImportSummary;
import com.piseth.java.school.roomservice.service.RoomImportService;
import com.piseth.java.school.roomservice.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

	
	private final RoomService roomService;
	private final RoomImportService importService;
	
	

	@PostMapping
	public Mono<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO) {

		return roomService.createRoom(roomDTO);
	}

	@GetMapping("/{roomId}")
	@Operation(summary = "Get room by id {Identity}",parameters = @Parameter(in=ParameterIn.PATH))
	public Mono<RoomDTO> getRoomById(@PathVariable String roomId) {
		return roomService.getRoomById(roomId);
	}

	@PutMapping("/{roomId}")
	public Mono<RoomDTO> updateRoom(@PathVariable String roomId, @RequestBody RoomDTO roomDTO) {

		return roomService.updateRoom(roomId, roomDTO);
	}

	@DeleteMapping("{id}")
	public Mono<Void> delete(@PathVariable String id) {
		return roomService.deleteRoom(id);
	}
	
	@GetMapping("room")
	@Operation(summary = "Get room by name ",parameters = @Parameter(in=ParameterIn.PATH))
	public Flux<RoomDTO> getRoomByName(@RequestParam String name) {
		return roomService.findRoomByName(name);
				
	}

	@GetMapping("/searches")
	public Flux<RoomDTO> getRoomByFilters(@ModelAttribute RoomFilterDTO dto) {
		return roomService.findByFilter(dto);
				
	}
	@GetMapping("/pagination")
	public Mono<PageDTO<RoomDTO>> getRoomByPagination(@ModelAttribute RoomFilterDTO dto) {
		return roomService.findByPagination(dto);
				
	}
	@GetMapping("/paginations")
	public Mono<ResponseEntity<PageDTO<RoomDTO>>> getRoomByPaginations(@ModelAttribute RoomFilterDTO dto) {
		
		return roomService.findByPagination(dto)
				.map(x->ResponseEntity.ok()
						.header("X-ray", String.valueOf(x.getTotalElement()))
						.body(x));
						
				
	}
	@PostMapping(value="/upload-excel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<RoomImportSummary> uploadExcell(@RequestPart("file")FilePart part){
		
		return importService.importRoom(part);
	}
	

}
