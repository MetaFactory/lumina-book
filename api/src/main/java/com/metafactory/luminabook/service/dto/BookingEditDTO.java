package com.metafactory.luminabook.service.dto;

import java.time.Instant;
import java.util.List;

public class BookingEditDTO {

    private Long id;
    private String guestEmail;
    private String guestName;
    private Instant startTime;
    private Instant endTime;
    private String status;
    private List<String> statusList;
    private String notes;
    private SelectOptionDTO selectedBookingPage;
    private List<SelectOptionDTO> bookingPageList;

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

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public SelectOptionDTO getSelectedBookingPage() {
        return selectedBookingPage;
    }

    public void setSelectedBookingPage(SelectOptionDTO selectedBookingPage) {
        this.selectedBookingPage = selectedBookingPage;
    }

    public List<SelectOptionDTO> getBookingPageList() {
        return bookingPageList;
    }

    public void setBookingPageList(List<SelectOptionDTO> bookingPageList) {
        this.bookingPageList = bookingPageList;
    }
}
