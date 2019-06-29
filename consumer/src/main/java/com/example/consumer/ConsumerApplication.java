package com.example.consumer;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import reactor.core.publisher.Flux;

@Log4j2
@EnableBinding(Sink.class)
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@StreamListener /*(Sink.INPUT)*/
	public void onNewGreetings(@Input(Sink.INPUT) Flux<Message> greetings) {

		greetings
			.map(Message::getName)
			.subscribe(msg -> log.info("new greeting: " + msg));
	}

/*	@KafkaListener(topics = "greetings")
	public void onNewGreetings(String greetings) {
		log.info("new greeting: " + greetings);
	}*/
}


@Data
class Message {
	Message() {
	}

	Message(String n) {
		this.name = n;
	}

	private String name;
}