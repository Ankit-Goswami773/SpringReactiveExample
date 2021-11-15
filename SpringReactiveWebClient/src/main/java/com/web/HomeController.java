package com.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HomeController {

	@Autowired
private WebClient.Builder webClient;

	@GetMapping("/transaction")
	public Flux<?> getAll()
	{
  Flux<Payload> payload=  webClient.build().get().uri("https://jsonmock.hackerrank.com/api/transactions/search")
		  .retrieve().bodyToFlux(Payload.class);
  System.out.println(payload);
    return payload;
	}
	@PostMapping("/save")
	public Mono<?> save(@RequestBody Employee employee)
	{
		Mono<Employee> emp=webClient.build().post().uri("http://localhost:8080/employee/add")
				.bodyValue(employee).retrieve().bodyToMono(Employee.class);
		System.out.println(emp.toString());
		return emp;	
	}
	@GetMapping("/api")
	public Mono<?> getAlTransaction()
	{
		Mono<String> s = webClient.build().get().uri("http://41.73.252.228:9191/GenericWaveTest/Proxy/vi/FetchBanks")
				.retrieve().bodyToMono(String.class);
		return s.flatMap(c -> {
			if (c != null) {
				ObjectMapper mapper = new ObjectMapper();
				List<Bank> bankList = new ArrayList<Bank>();
				try {
					Map<String, String> bankMap = mapper.readValue(c, Map.class);
					for (Map.Entry<String, String> entry : bankMap.entrySet()) {
						Bank bankDetails = new Bank();
						bankDetails.setBankname(entry.getKey());
						bankDetails.setBankcode(entry.getValue());
						bankList.add(bankDetails);
						
					}
					return Mono.just(bankList);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}

			return null;
		});

	}
	}
	