package ru.sokol.smartoffice.controller;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class CaptchaController {
    private final AtomicInteger elementNumber = new AtomicInteger(0);
    private final List<byte[]> cachedCaptchaFile;

    @SneakyThrows
    public CaptchaController() {
        cachedCaptchaFile = Arrays.stream((new ClassPathResource("/captcha/")).getFile().listFiles()).map(File::toPath).map((Path path) -> {
            try {
                return Files.readAllBytes(path);
            } catch (IOException ignored) {
            }
            return new byte[1];
        }).collect(Collectors.toUnmodifiableList());
    }

    private byte[] getNextFile(){
        return cachedCaptchaFile.get( elementNumber.getAndIncrement() % cachedCaptchaFile.size());
    }

    @GetMapping("/reactive-captcha")
    Mono<ResponseEntity<?>> reactiveCaptchaDownload(){
        return Mono.delay(Duration.ofSeconds(6)).map(x ->  ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(getNextFile()));

    }
}
