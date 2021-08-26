package ru.proskuryakov.testcbrcursonadapter.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitLoggingConfig {

    @Value("${rabbitmq.queue.logging}")
    private String queueName;

    @Value("${rabbitmq.exchange.logging}")
    private String exchange;

    @Value("${rabbitmq.routingkey.logging}")
    private String routingkey;

    @Bean
    Queue loggingQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange loggingExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding loggingBinding(@Qualifier("loggingQueue") Queue queue, @Qualifier("loggingExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
