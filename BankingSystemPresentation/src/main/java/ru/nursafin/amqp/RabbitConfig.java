package ru.nursafin.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${app.rabbit.rates-exchange}")
    private String ratesExchange;

    @Value("${app.rabbit.rates-queue}")
    private String ratesQueue;

    @Bean
    public TopicExchange ratesExchange() {
        return new TopicExchange(ratesExchange);
    }

    @Bean
    public Queue ratesQueue() {
        return new Queue(ratesQueue, true);
    }

    @Bean
    public Binding ratesBinding(Queue ratesQueue, TopicExchange ratesExchange) {
        return BindingBuilder.bind(ratesQueue).to(ratesExchange).with("rates.#");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
