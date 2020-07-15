package ru.sokol.smartoffice.controller;

import lombok.SneakyThrows;
import org.apache.tomcat.jni.Time;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.io.InputStream;
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
    public CaptchaController(ApplicationContext applicationContext) {

        cachedCaptchaFile = Arrays.stream(
                applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/captcha/*.png"))
                .map(CaptchaController::getInputStreamSneakyThrow)
                .map(CaptchaController::readAllBytesSneakyThrow)
                .collect(Collectors.toUnmodifiableList());
                /*.map(File::toPath).map((Path path) -> {
            try {
                return Files.readAllBytes(path);
            } catch (IOException ignored) {
            }
            return new byte[1];
        }).collect(Collectors.toUnmodifiableList());*/
    }

    private byte[] getNextFile() {
        return cachedCaptchaFile.get(elementNumber.getAndIncrement() % cachedCaptchaFile.size());
    }

    @SneakyThrows
    private static InputStream getInputStreamSneakyThrow(InputStreamSource streamSource) {
        return streamSource.getInputStream();
    }

    @SneakyThrows
    private static byte[] readAllBytesSneakyThrow(InputStream inputStream) {
        return inputStream.readAllBytes();
    }

    @GetMapping("/captcha")
    ResponseEntity<?> captchaDownload() {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(getNextFile());
    }

    @GetMapping("/captcha/slept")
    ResponseEntity<?> sleptCaptchaDownload() {
        Time.sleep(6000);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(getNextFile());
    }

    @GetMapping("/captcha/reactive")
    Mono<ResponseEntity<?>> reactiveCaptchaDownload() {
        return Mono.delay(Duration.ofSeconds(6)).map(x -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(getNextFile()));

    }
}
