package ru.proskuryakov.testcbrcursonadapter.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RabbitLoggingListener {

    private final Logger log = LoggerFactory.getLogger(RabbitLoggingListener.class);
    private final Queue<LoggingMessage> messageQueue = new ConcurrentLinkedQueue<>();

    @RabbitListener(queues = "${rabbitmq.queue.logging}")
    public void receiveMessage(LoggingMessage loggingMessage) {
        messageQueue.add(loggingMessage);
        log.info("Received message: {}", loggingMessage);
    }

    public LoggingMessage getMessage() {
        return messageQueue.poll();
    }

}
