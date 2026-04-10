package com.metafactory.luminabook.service.dto;

import com.metafactory.luminabook.domain.Booking;
import com.metafactory.luminabook.domain.enumeration.BookingStatus;
import java.time.Instant;

public class BookingListItemDTO {

    private Long id;
    private String guestEmail;
    private String guestName;
    private Instant startTime;
    private Instant endTime;
    private String status;
    private String statusDisplayValue;
    private String notes;
    private Long bookingPagePkId;
    private String bookingPageDisplayValue;

    public BookingListItemDTO() {}

    public BookingListItemDTO(Booking booking) {
        this.id = booking.getId();
        this.guestEmail = booking.getGuestEmail();
        this.guestName = booking.getGuestName();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
        this.notes = booking.getNotes();

        if (booking.getStatus() != null) {
            this.status = booking.getStatus().name();
            this.statusDisplayValue = "BookingStatus." + booking.getStatus().name();
        }

        if (booking.getBookingPage() != null) {
            this.bookingPagePkId = booking.getBookingPage().getId();
            this.bookingPageDisplayValue = booking.getBookingPage().getTitle();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplayValue() {
        return statusDisplayValue;
    }

    public void setStatusDisplayValue(String statusDisplayValue) {
        this.statusDisplayValue = statusDisplayValue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getBookingPagePkId() {
        return bookingPagePkId;
    }

    public void setBookingPagePkId(Long bookingPagePkId) {
        this.bookingPagePkId = bookingPagePkId;
    }

    public String getBookingPageDisplayValue() {
        return bookingPageDisplayValue;
    }

    public void setBookingPageDisplayValue(String bookingPageDisplayValue) {
        this.bookingPageDisplayValue = bookingPageDisplayValue;
    }
}
