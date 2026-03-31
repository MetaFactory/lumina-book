package com.metafactory.luminabook.domain;

import static com.metafactory.luminabook.domain.BookingPageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.metafactory.luminabook.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingPage.class);
        BookingPage bookingPage1 = getBookingPageSample1();
        BookingPage bookingPage2 = new BookingPage();
        assertThat(bookingPage1).isNotEqualTo(bookingPage2);

        bookingPage2.setId(bookingPage1.getId());
        assertThat(bookingPage1).isEqualTo(bookingPage2);

        bookingPage2 = getBookingPageSample2();
        assertThat(bookingPage1).isNotEqualTo(bookingPage2);
    }
}
