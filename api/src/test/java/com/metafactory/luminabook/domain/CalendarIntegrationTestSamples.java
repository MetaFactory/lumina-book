package com.metafactory.luminabook.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CalendarIntegrationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CalendarIntegration getCalendarIntegrationSample1() {
        return new CalendarIntegration()
            .id(1L)
            .accessToken("accessToken1")
            .refreshToken("refreshToken1")
            .externalCalendarId("externalCalendarId1");
    }

    public static CalendarIntegration getCalendarIntegrationSample2() {
        return new CalendarIntegration()
            .id(2L)
            .accessToken("accessToken2")
            .refreshToken("refreshToken2")
            .externalCalendarId("externalCalendarId2");
    }

    public static CalendarIntegration getCalendarIntegrationRandomSampleGenerator() {
        return new CalendarIntegration()
            .id(longCount.incrementAndGet())
            .accessToken(UUID.randomUUID().toString())
            .refreshToken(UUID.randomUUID().toString())
            .externalCalendarId(UUID.randomUUID().toString());
    }
}
