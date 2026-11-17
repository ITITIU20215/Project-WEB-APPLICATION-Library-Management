package com.library.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Single source of "now". Inject {@link Clock} instead of calling {@code LocalDate.now()} so tests
 * can fix today's date (due dates, fines, hold expiry).
 */
@Configuration(proxyBeanMethods = false)
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
