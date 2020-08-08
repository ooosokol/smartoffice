package ru.sokol.smartoffice.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;

@Component
public class DeviceApiBusSender {
    private final RabbitTemplate template;
    private final TopicExchange topic;
    private final ObjectMapper mapper;

    public DeviceApiBusSender(RabbitTemplate template, TopicExchange topic, ObjectMapper mapper) {
        this.template = template;
        this.topic = topic;
        this.mapper = mapper;
    }

    public void send(DeviceControlRequest request) {
        try {
            template.convertAndSend(topic.getName(), "hardware.device.control.request", mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
