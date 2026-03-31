package com.metafactory.luminabook.web.rest;

import com.metafactory.luminabook.domain.BookingPage;
import com.metafactory.luminabook.repository.BookingPageRepository;
import com.metafactory.luminabook.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.metafactory.luminabook.domain.BookingPage}.
 */
@RestController
@RequestMapping("/api/booking-pages")
@Transactional
public class BookingPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingPageResource.class);

    private static final String ENTITY_NAME = "bookingPage";

    @Value("${jhipster.clientApp.name:luminaBook}")
    private String applicationName;

    private final BookingPageRepository bookingPageRepository;

    public BookingPageResource(BookingPageRepository bookingPageRepository) {
        this.bookingPageRepository = bookingPageRepository;
    }

    /**
     * {@code POST  /booking-pages} : Create a new bookingPage.
     *
     * @param bookingPage the bookingPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingPage, or with status {@code 400 (Bad Request)} if the bookingPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingPage> createBookingPage(@Valid @RequestBody BookingPage bookingPage) throws URISyntaxException {
        LOG.debug("REST request to save BookingPage : {}", bookingPage);
        if (bookingPage.getId() != null) {
            throw new BadRequestAlertException("A new bookingPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingPage = bookingPageRepository.save(bookingPage);
        return ResponseEntity.created(new URI("/api/booking-pages/" + bookingPage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingPage.getId().toString()))
            .body(bookingPage);
    }

    /**
     * {@code PUT  /booking-pages/:id} : Updates an existing bookingPage.
     *
     * @param id the id of the bookingPage to save.
     * @param bookingPage the bookingPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingPage,
     * or with status {@code 400 (Bad Request)} if the bookingPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingPage> updateBookingPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingPage bookingPage
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingPage : {}, {}", id, bookingPage);
        if (bookingPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingPage = bookingPageRepository.save(bookingPage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingPage.getId().toString()))
            .body(bookingPage);
    }

    /**
     * {@code PATCH  /booking-pages/:id} : Partial updates given fields of an existing bookingPage, field will ignore if it is null
     *
     * @param id the id of the bookingPage to save.
     * @param bookingPage the bookingPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingPage,
     * or with status {@code 400 (Bad Request)} if the bookingPage is not valid,
     * or with status {@code 404 (Not Found)} if the bookingPage is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingPage> partialUpdateBookingPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingPage bookingPage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingPage partially : {}, {}", id, bookingPage);
        if (bookingPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingPage> result = bookingPageRepository
            .findById(bookingPage.getId())
            .map(existingBookingPage -> {
                updateIfPresent(existingBookingPage::setSlug, bookingPage.getSlug());
                updateIfPresent(existingBookingPage::setTitle, bookingPage.getTitle());
                updateIfPresent(existingBookingPage::setDescription, bookingPage.getDescription());
                updateIfPresent(existingBookingPage::setDurationMinutes, bookingPage.getDurationMinutes());
                updateIfPresent(existingBookingPage::setActive, bookingPage.getActive());

                return existingBookingPage;
            })
            .map(bookingPageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingPage.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-pages} : get all the Booking Pages.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Booking Pages in body.
     */
    @GetMapping("")
    public List<BookingPage> getAllBookingPages(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all BookingPages");
        if (eagerload) {
            return bookingPageRepository.findAllWithEagerRelationships();
        } else {
            return bookingPageRepository.findAll();
        }
    }

    /**
     * {@code GET  /booking-pages/:id} : get the "id" bookingPage.
     *
     * @param id the id of the bookingPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingPage> getBookingPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingPage : {}", id);
        Optional<BookingPage> bookingPage = bookingPageRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bookingPage);
    }

    /**
     * {@code DELETE  /booking-pages/:id} : delete the "id" bookingPage.
     *
     * @param id the id of the bookingPage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingPage : {}", id);
        bookingPageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
