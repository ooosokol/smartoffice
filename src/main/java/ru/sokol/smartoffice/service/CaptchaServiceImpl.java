package ru.sokol.smartoffice.service;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    private final Duration BASE_CAPTCHA_LATENCY_DURATION = Duration.ofMillis(4369);

    private final AtomicLong elementNumber = new AtomicLong(0);
    private final List<byte[]> cachedCaptchaFile;
    private final Random random = new Random();

    @SneakyThrows
    public CaptchaServiceImpl(ApplicationContext applicationContext) {

        cachedCaptchaFile = Arrays.stream(
                applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/captcha/*.png"))
                .map(CaptchaServiceImpl::getInputStreamSneakyThrow)
                .map(CaptchaServiceImpl::readAllBytesSneakyThrow)
                .collect(Collectors.toUnmodifiableList());
    }


    @Override
    public Mono<byte[]> getNextCaptcha() {
        return Mono.delay(BASE_CAPTCHA_LATENCY_DURATION.plusMillis(random.nextInt(1632))).map(x -> getNextFile());

    }

    @SneakyThrows
    private static InputStream getInputStreamSneakyThrow(InputStreamSource streamSource) {
        return streamSource.getInputStream();
    }

    @SneakyThrows
    private static byte[] readAllBytesSneakyThrow(InputStream inputStream) {
        return inputStream.readAllBytes();
    }

    private byte[] getNextFile() {
        return cachedCaptchaFile.get((int) (elementNumber.getAndIncrement() % cachedCaptchaFile.size()));
    }


}
