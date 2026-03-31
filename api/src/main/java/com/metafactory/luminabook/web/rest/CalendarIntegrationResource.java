package com.metafactory.luminabook.web.rest;

import com.metafactory.luminabook.domain.CalendarIntegration;
import com.metafactory.luminabook.repository.CalendarIntegrationRepository;
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
 * REST controller for managing {@link com.metafactory.luminabook.domain.CalendarIntegration}.
 */
@RestController
@RequestMapping("/api/calendar-integrations")
@Transactional
public class CalendarIntegrationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalendarIntegrationResource.class);

    private static final String ENTITY_NAME = "calendarIntegration";

    @Value("${jhipster.clientApp.name:luminaBook}")
    private String applicationName;

    private final CalendarIntegrationRepository calendarIntegrationRepository;

    public CalendarIntegrationResource(CalendarIntegrationRepository calendarIntegrationRepository) {
        this.calendarIntegrationRepository = calendarIntegrationRepository;
    }

    /**
     * {@code POST  /calendar-integrations} : Create a new calendarIntegration.
     *
     * @param calendarIntegration the calendarIntegration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarIntegration, or with status {@code 400 (Bad Request)} if the calendarIntegration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CalendarIntegration> createCalendarIntegration(@Valid @RequestBody CalendarIntegration calendarIntegration)
        throws URISyntaxException {
        LOG.debug("REST request to save CalendarIntegration : {}", calendarIntegration);
        if (calendarIntegration.getId() != null) {
            throw new BadRequestAlertException("A new calendarIntegration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        calendarIntegration = calendarIntegrationRepository.save(calendarIntegration);
        return ResponseEntity.created(new URI("/api/calendar-integrations/" + calendarIntegration.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, calendarIntegration.getId().toString()))
            .body(calendarIntegration);
    }

    /**
     * {@code PUT  /calendar-integrations/:id} : Updates an existing calendarIntegration.
     *
     * @param id the id of the calendarIntegration to save.
     * @param calendarIntegration the calendarIntegration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarIntegration,
     * or with status {@code 400 (Bad Request)} if the calendarIntegration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarIntegration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarIntegration> updateCalendarIntegration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CalendarIntegration calendarIntegration
    ) throws URISyntaxException {
        LOG.debug("REST request to update CalendarIntegration : {}, {}", id, calendarIntegration);
        if (calendarIntegration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarIntegration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarIntegrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        calendarIntegration = calendarIntegrationRepository.save(calendarIntegration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calendarIntegration.getId().toString()))
            .body(calendarIntegration);
    }

    /**
     * {@code PATCH  /calendar-integrations/:id} : Partial updates given fields of an existing calendarIntegration, field will ignore if it is null
     *
     * @param id the id of the calendarIntegration to save.
     * @param calendarIntegration the calendarIntegration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarIntegration,
     * or with status {@code 400 (Bad Request)} if the calendarIntegration is not valid,
     * or with status {@code 404 (Not Found)} if the calendarIntegration is not found,
     * or with status {@code 500 (Internal Server Error)} if the calendarIntegration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalendarIntegration> partialUpdateCalendarIntegration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CalendarIntegration calendarIntegration
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CalendarIntegration partially : {}, {}", id, calendarIntegration);
        if (calendarIntegration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarIntegration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarIntegrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalendarIntegration> result = calendarIntegrationRepository
            .findById(calendarIntegration.getId())
            .map(existingCalendarIntegration -> {
                updateIfPresent(existingCalendarIntegration::setProvider, calendarIntegration.getProvider());
                updateIfPresent(existingCalendarIntegration::setAccessToken, calendarIntegration.getAccessToken());
                updateIfPresent(existingCalendarIntegration::setRefreshToken, calendarIntegration.getRefreshToken());
                updateIfPresent(existingCalendarIntegration::setExternalCalendarId, calendarIntegration.getExternalCalendarId());

                return existingCalendarIntegration;
            })
            .map(calendarIntegrationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calendarIntegration.getId().toString())
        );
    }

    /**
     * {@code GET  /calendar-integrations} : get all the Calendar Integrations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Calendar Integrations in body.
     */
    @GetMapping("")
    public List<CalendarIntegration> getAllCalendarIntegrations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CalendarIntegrations");
        if (eagerload) {
            return calendarIntegrationRepository.findAllWithEagerRelationships();
        } else {
            return calendarIntegrationRepository.findAll();
        }
    }

    /**
     * {@code GET  /calendar-integrations/:id} : get the "id" calendarIntegration.
     *
     * @param id the id of the calendarIntegration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarIntegration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarIntegration> getCalendarIntegration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CalendarIntegration : {}", id);
        Optional<CalendarIntegration> calendarIntegration = calendarIntegrationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(calendarIntegration);
    }

    /**
     * {@code DELETE  /calendar-integrations/:id} : delete the "id" calendarIntegration.
     *
     * @param id the id of the calendarIntegration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendarIntegration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CalendarIntegration : {}", id);
        calendarIntegrationRepository.deleteById(id);
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
