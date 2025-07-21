package com.piseth.java.school.roomservice.problem.factory.impl;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.piseth.java.school.roomservice.problem.factory.ProblemDetailFactory;
@Component
public class DefaltProblemDetailImpl implements ProblemDetailFactory {

	@Override
	public ProblemDetail create(HttpStatus status, String message, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		return create(status, message, status.name(), exchange);
	}

	@Override
	public ProblemDetail create(HttpStatus status, String message, ServerWebExchange exchange,
			Map<String, String> properties) {
		ProblemDetail detail=create(status, message, exchange);
			if(Objects.nonNull(properties)) {
				properties.forEach(detail::setProperty);
			}
		return detail;
	}

	@Override
	public ProblemDetail create(HttpStatus status, String message, String errorCode, ServerWebExchange exchange) {
			ProblemDetail detail=ProblemDetail.forStatusAndDetail(status, message);
			detail.setTitle(status.getReasonPhrase());
			detail.setProperty("timestamp ", Instant.now());
			detail.setProperty("path  ",exchange.getRequest().getPath().value());
			detail.setProperty("ERROR_CODE ",errorCode);
		return detail;
	}

}
