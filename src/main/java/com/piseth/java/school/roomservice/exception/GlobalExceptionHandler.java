package com.piseth.java.school.roomservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import com.piseth.java.school.roomservice.enumming.ErrorCode;
import com.piseth.java.school.roomservice.problem.factory.ProblemDetailFactory;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final ProblemDetailFactory factory;

	@ExceptionHandler(RoomNotFoundException.class)
	public Mono<ProblemDetail> handleRoomNotFound(RoomNotFoundException e,ServerWebExchange exchange){
		log.warn("Room not found : {}",e.getMessage());
//		ProblemDetail forStatusAndDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
//		forStatusAndDetail.setTitle(ErrorCode.NOT_FOUND.name());
		return Mono.just(factory.create(HttpStatus.NOT_FOUND,
				e.getMessage(),
				ErrorCode.NOT_FOUND.name(),
				exchange));
		
	}
	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ProblemDetail> handleConstraintViolation(WebExchangeBindException e,ServerWebExchange exchange){
		log.warn("Constraint violation : {}",e.getMessage());
//		ProblemDetail forStatusAndDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
//		forStatusAndDetail.setTitle(ErrorCode.CONSTRAINT_VIOLATION.name());
		return Mono.just(factory.create(HttpStatus.BAD_REQUEST,
				e.getMessage(),
				ErrorCode.CONSTRAINT_VIOLATION.name(), 
				exchange));
	}
	@ExceptionHandler(Exception.class)
	public Mono<ProblemDetail> handleGeneric(Exception ex, ServerWebExchange exchange){
		log.warn("Unexpected Error: {}", ex.getMessage());
		return Mono.just(factory.create(
				HttpStatus.INTERNAL_SERVER_ERROR, 
				"Unexpected error: " + ex.getMessage(),
				ErrorCode.SYSTEM_ERROR.name() ,
				exchange));
		
	}

}
