package com.example.producer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableBinding(Source.class)
@SpringBootApplication
public class ProducerApplication {

	private final MessageChannel output;

	ProducerApplication(Source source) {
		this.output = source.output();
	}

//	private final String topic = "greetings";

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

/*
	private final KafkaTemplate<String, String> template;

	ProducerApplication(KafkaTemplate<String, String> template) {
		this.template = template;
	}

	private void sendGreetingTo(String name) {
		this.template.send(topic, "llave", "Hola " + name + "!");
	}

*/

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route()
			.POST("/greet/{name}", r -> {
				var name = r.pathVariable("name");
				sendGreetingTo(name);
				return ServerResponse.ok().syncBody(true);
			})
			.build();
	}


	private void sendGreetingTo(String name) {
		var message = MessageBuilder.withPayload(new Message(name)).build();
		output.send(message);
	}
}

@Data
@RequiredArgsConstructor
class Message {
	private final String name;
}