package com.metafactory.luminabook.service;

import com.metafactory.luminabook.domain.Booking;
import com.metafactory.luminabook.domain.BookingPage;
import com.metafactory.luminabook.domain.enumeration.BookingStatus;
import com.metafactory.luminabook.repository.BookingPageRepository;
import com.metafactory.luminabook.repository.BookingRepository;
import com.metafactory.luminabook.service.dto.BookingEditDTO;
import com.metafactory.luminabook.service.dto.BookingListItemDTO;
import com.metafactory.luminabook.service.dto.SelectOptionDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BookingPageRepository bookingPageRepository;

    public BookingService(BookingRepository bookingRepository, BookingPageRepository bookingPageRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingPageRepository = bookingPageRepository;
    }

    @Transactional(readOnly = true)
    public Page<BookingListItemDTO> findAll(Pageable pageable, String status, Long bookingPageId) {
        Page<Booking> page;
        if (status != null && bookingPageId != null) {
            page = bookingRepository.findByStatusAndBookingPageId(BookingStatus.valueOf(status), bookingPageId, pageable);
        } else if (status != null) {
            page = bookingRepository.findByStatus(BookingStatus.valueOf(status), pageable);
        } else if (bookingPageId != null) {
            page = bookingRepository.findByBookingPageId(bookingPageId, pageable);
        } else {
            page = bookingRepository.findAllWithEagerRelationships(pageable);
        }
        return page.map(BookingListItemDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<BookingListItemDTO> findOne(Long id) {
        return bookingRepository.findOneWithEagerRelationships(id).map(BookingListItemDTO::new);
    }

    @Transactional(readOnly = true)
    public BookingEditDTO getEditDTO(Long id) {
        Booking booking = bookingRepository.findOneWithEagerRelationships(id)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        return toEditDTO(booking);
    }

    @Transactional(readOnly = true)
    public BookingEditDTO getNewEditDTO() {
        BookingEditDTO dto = new BookingEditDTO();
        dto.setStatus(BookingStatus.PENDING.name());
        dto.setStatusList(getStatusOptions());
        dto.setBookingPageList(getBookingPageOptions());
        return dto;
    }

    public BookingEditDTO save(BookingEditDTO editDTO) {
        Booking booking;
        if (editDTO.getId() != null) {
            booking = bookingRepository.findById(editDTO.getId())
                .orElseThrow(() -> new RuntimeException("Booking not found: " + editDTO.getId()));
        } else {
            booking = new Booking();
        }

        booking.setGuestEmail(editDTO.getGuestEmail());
        booking.setGuestName(editDTO.getGuestName());
        booking.setStartTime(editDTO.getStartTime());
        booking.setEndTime(editDTO.getEndTime());
        booking.setNotes(editDTO.getNotes());

        if (editDTO.getStatus() != null) {
            booking.setStatus(BookingStatus.valueOf(editDTO.getStatus()));
        }

        if (editDTO.getSelectedBookingPage() != null && editDTO.getSelectedBookingPage().getId() != null) {
            Long bookingPageId = ((Number) editDTO.getSelectedBookingPage().getId()).longValue();
            BookingPage bookingPage = bookingPageRepository.findById(bookingPageId)
                .orElseThrow(() -> new RuntimeException("BookingPage not found: " + bookingPageId));
            booking.setBookingPage(bookingPage);
        } else {
            booking.setBookingPage(null);
        }

        booking = bookingRepository.save(booking);
        return toEditDTO(booking);
    }

    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingEditDTO toEditDTO(Booking booking) {
        BookingEditDTO dto = new BookingEditDTO();
        dto.setId(booking.getId());
        dto.setGuestEmail(booking.getGuestEmail());
        dto.setGuestName(booking.getGuestName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setNotes(booking.getNotes());

        if (booking.getStatus() != null) {
            dto.setStatus(booking.getStatus().name());
        }

        dto.setStatusList(getStatusOptions());

        if (booking.getBookingPage() != null) {
            dto.setSelectedBookingPage(
                new SelectOptionDTO(booking.getBookingPage().getId(), booking.getBookingPage().getTitle())
            );
        }
        dto.setBookingPageList(getBookingPageOptions());

        return dto;
    }

    private List<String> getStatusOptions() {
        return Arrays.stream(BookingStatus.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    private List<SelectOptionDTO> getBookingPageOptions() {
        return bookingPageRepository.findAll().stream()
            .map(bp -> new SelectOptionDTO(bp.getId(), bp.getTitle()))
            .collect(Collectors.toList());
    }
}
