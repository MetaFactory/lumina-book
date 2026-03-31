package com.metafactory.luminabook.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Booking getBookingSample1() {
        return new Booking().id(1L).guestEmail("guestEmail1").guestName("guestName1").notes("notes1");
    }

    public static Booking getBookingSample2() {
        return new Booking().id(2L).guestEmail("guestEmail2").guestName("guestName2").notes("notes2");
    }

    public static Booking getBookingRandomSampleGenerator() {
        return new Booking()
            .id(longCount.incrementAndGet())
            .guestEmail(UUID.randomUUID().toString())
            .guestName(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
