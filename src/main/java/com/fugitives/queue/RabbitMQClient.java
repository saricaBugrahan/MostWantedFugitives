package com.fugitives.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.LoggerFactory;
import com.fugitives.scraping.Fugitive;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class RabbitMQClient {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RabbitMQClient.class);
    private final String QUEUE_NAME = "fugitives";
    private final ConnectionFactory factory;
    private final Logger logger = Logger.getLogger(RabbitMQClient.class.getName());
    public RabbitMQClient(){
        factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
    }

    public void sendMessage(Fugitive fugitive){
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, fugitive.toJson().getBytes());

        } catch (TimeoutException timeoutException){
            logger.warning("RabbitMQ Connection Timeout"+timeoutException.getMessage());
        } catch (IOException ioException){
            logger.warning("RabbitMQ Connection Error"+ioException.getMessage());
        }
    }
}
