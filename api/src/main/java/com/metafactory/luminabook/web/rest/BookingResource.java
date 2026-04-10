package com.metafactory.luminabook.web.rest;

import com.metafactory.luminabook.service.BookingService;
import com.metafactory.luminabook.service.dto.BookingEditDTO;
import com.metafactory.luminabook.service.dto.BookingListItemDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingResource.class);

    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllBookings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long bookingPageId
    ) {
        LOG.debug("REST request to get a page of Bookings");

        Page<BookingListItemDTO> page = bookingService.findAll(pageable, status, bookingPageId);

        Map<String, Object> response = new HashMap<>();
        response.put("items", page.getContent());
        response.put("pageNumber", page.getNumber());
        response.put("pageSize", page.getSize());
        response.put("pageCount", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingListItemDTO> getBooking(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Booking : {}", id);
        return bookingService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/edit/new")
    public ResponseEntity<BookingEditDTO> getNewBooking() {
        LOG.debug("REST request to get new Booking edit form");
        return ResponseEntity.ok(bookingService.getNewEditDTO());
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<BookingEditDTO> getEditBooking(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Booking edit form : {}", id);
        return ResponseEntity.ok(bookingService.getEditDTO(id));
    }

    @PutMapping("/edit")
    public ResponseEntity<BookingEditDTO> upsertBooking(@RequestBody BookingEditDTO editDTO) {
        LOG.debug("REST request to upsert Booking");
        BookingEditDTO result = bookingService.save(editDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> getFilters() {
        LOG.debug("REST request to get Booking filters");
        return ResponseEntity.ok(Map.of("filters", Map.of()));
    }
}
