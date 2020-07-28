package ru.sokol.smartoffice.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.sokol.smartoffice.service.CaptchaService;

@Controller
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/captcha")
    Publisher<ResponseEntity<?>> reactiveCaptchaDownload() {
        return captchaService.getNextCaptcha().map(fileContent -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(fileContent));

    }
}
