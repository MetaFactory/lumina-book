package com.metafactory.luminabook.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookingPageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BookingPage getBookingPageSample1() {
        return new BookingPage().id(1L).slug("slug1").title("title1").description("description1").durationMinutes(1);
    }

    public static BookingPage getBookingPageSample2() {
        return new BookingPage().id(2L).slug("slug2").title("title2").description("description2").durationMinutes(2);
    }

    public static BookingPage getBookingPageRandomSampleGenerator() {
        return new BookingPage()
            .id(longCount.incrementAndGet())
            .slug(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .durationMinutes(intCount.incrementAndGet());
    }
}
