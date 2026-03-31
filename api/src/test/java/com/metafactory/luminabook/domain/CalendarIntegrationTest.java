package com.metafactory.luminabook.domain;

import static com.metafactory.luminabook.domain.CalendarIntegrationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.metafactory.luminabook.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendarIntegrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarIntegration.class);
        CalendarIntegration calendarIntegration1 = getCalendarIntegrationSample1();
        CalendarIntegration calendarIntegration2 = new CalendarIntegration();
        assertThat(calendarIntegration1).isNotEqualTo(calendarIntegration2);

        calendarIntegration2.setId(calendarIntegration1.getId());
        assertThat(calendarIntegration1).isEqualTo(calendarIntegration2);

        calendarIntegration2 = getCalendarIntegrationSample2();
        assertThat(calendarIntegration1).isNotEqualTo(calendarIntegration2);
    }
}
