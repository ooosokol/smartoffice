package ru.sokol.smartoffice.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sokol.smartoffice.service.CaptchaService;

@Controller
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/captcha/reactive")
    Publisher<ResponseEntity<?>> reactiveCaptchaDownload() {
        return captchaService.getNextCaptcha().map(fileContent -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(fileContent));

    }
}
