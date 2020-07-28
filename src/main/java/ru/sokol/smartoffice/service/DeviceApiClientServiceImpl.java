package ru.sokol.smartoffice.service;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlResponse;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DeviceApiClientServiceImpl implements DeviceApiClientService {

    @Value("device.api.base.url")
    String baseUrl;

    private final WebClient webClient;

    public DeviceApiClientServiceImpl() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(tcpClient -> {
                    tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                    tcpClient = tcpClient.doOnConnected(conn -> conn
                            .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
                    return tcpClient;
                });
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        this.webClient = WebClient.builder()
                .clientConnector(connector)
                .baseUrl(baseUrl)
                .build();

    }

    @Override
    public Mono<DeviceControlResponse> processRequest(DeviceControlRequest request) {
       return webClient.post().uri("/process").body(Mono.just(request), DeviceControlRequest.class)
                .retrieve().bodyToMono(DeviceControlResponse.class);
    }

}
