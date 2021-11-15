package com.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TranscationController {

	
	@Autowired
	private WebClient.Builder webClient;
	
	public Mono<Payload> getFromApi(int uid, int page) {
		String url="https://jsonmock.hackerrank.com/api/transactions/search?userId="+uid+ "&page=" + page;
		return webClient.build().get().uri(url)
		  .retrieve().bodyToMono(Payload.class);
	}
	
	@GetMapping("/bank")
	public Mono<?> getAll(@RequestParam(name = "uid") int uid,
			@RequestParam(name = "txnType") String txnType, 
			@RequestParam(name = "monthYear") String monthYear)
	{
		
		Mono<Payload> payload = this.getFromApi(uid,1);
          Mono<Integer> total = payload.map(s->s.getTotal_pages());
		 
		 return total.flatMap(totalpage->{
 List<Mono<Payload>> listOfMonoOfTransactionRes = new ArrayList<Mono<Payload>>();	 
			 for(int i=1;i<=totalpage;i++)
			 {
				 listOfMonoOfTransactionRes.add(this.getFromApi(uid, i));
			 }
			
	return Flux.fromIterable(listOfMonoOfTransactionRes).flatMap(s->s).collectList()
			.flatMap(a->{
				List<Integer>  ids=new ArrayList<Integer>();
				if(!monthYear.equals("") && (txnType.equals("debit")))
				{
				int	month1=Integer.parseInt(monthYear.substring(0,2));
			    int	year=Integer.parseInt(monthYear.substring(3));
			    
		List<Double> collect = a.stream().flatMap(transectionApi->transectionApi.getData().stream())
		.filter(s->s.getTxnType().equals("debit") && (s.month()==month1 && s.year()==year))
		.map(m->m.parseAmoutn()).collect(Collectors.toList());
		
				double average = collect.stream().mapToDouble(s->s).average().orElse(0.0);
				
				ids = a.stream().flatMap(r->r.getData().stream())
				.filter(data->data.getTxnType().equals("debit") && (data.month()==month1 && data.year()==year) 
						&& data.parseAmoutn() > average)
				.map(k->k.getId()).collect(Collectors.toList());
			return Mono.just(ids);	
				}
				else
				{
					
				if(monthYear.equals("") && (txnType.equals("")))
				{
				ids=a.stream().flatMap(s->s.getData().stream()).filter(b->b.getId()==uid).map(r->r.getId()).collect(Collectors.toList());
				return Mono.just(ids);
				}
				else if(!txnType.equals(""))
				{
		  List<Transaction> collect = a.stream().flatMap(s->s.getData().stream()).filter(c->c.getTxnType().equals(txnType))
		  .collect(Collectors.toList());
		  double average = collect.stream().mapToDouble(s->s.getId()).average().orElse(0.0);
		ids=		a.stream().flatMap(s->s.getData().stream()).filter(c->c.getTxnType().equals(txnType) && (c.parseAmoutn()>average))
				.map(i->i.getId()).collect(Collectors.toList());
		
		return Mono.just(ids);
				}
				else
				{
	int	month1=Integer.parseInt(monthYear.substring(0,2));
	    int	year=Integer.parseInt(monthYear.substring(3));
				    
	    List<Transaction> collect = a.stream().flatMap(s->s.getData().stream()).filter(t->t.month()==month1 && (t.year()==year))
		    .collect(Collectors.toList());
				    
	    double average = collect.stream().mapToDouble(s->s.getId()).average().orElse(0.0);
		ids= a.stream().flatMap(s->s.getData().stream()).filter(t->t.month()==month1 && (t.year()==year)).map(r->r.getId())
		    .collect(Collectors.toList());
				return Mono.just(ids);
				}
				}
				
			});
		 });
		 
	
}
	
	
	
	
	
	
}