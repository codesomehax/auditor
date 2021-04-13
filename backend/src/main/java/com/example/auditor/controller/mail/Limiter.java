package com.example.auditor.controller.mail;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class Limiter {
    private static Bucket bucket;

    public static Bucket get() {
        if (bucket == null) {
            Bandwidth limit = Bandwidth.classic(2, Refill.greedy(1, Duration.ofSeconds(5)));
            bucket = Bucket4j.builder()
                    .addLimit(limit).build();
        }
        return bucket;
    }
}
