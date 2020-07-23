package ru.sokol.smartoffice.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MegatronServiceImpl {
    private AtomicLong counter = new AtomicLong();

    private static final String secret = "04080F1017A2";


}
