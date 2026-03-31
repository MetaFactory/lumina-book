package com.metafactory.luminabook.domain;

import static com.metafactory.luminabook.domain.BookingPageTestSamples.*;
import static com.metafactory.luminabook.domain.BookingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.metafactory.luminabook.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void bookingPageTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingPage bookingPageBack = getBookingPageRandomSampleGenerator();

        booking.setBookingPage(bookingPageBack);
        assertThat(booking.getBookingPage()).isEqualTo(bookingPageBack);

        booking.bookingPage(null);
        assertThat(booking.getBookingPage()).isNull();
    }
}
