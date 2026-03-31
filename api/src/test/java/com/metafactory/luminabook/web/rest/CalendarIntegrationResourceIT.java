package com.metafactory.luminabook.web.rest;

import static com.metafactory.luminabook.domain.CalendarIntegrationAsserts.*;
import static com.metafactory.luminabook.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metafactory.luminabook.IntegrationTest;
import com.metafactory.luminabook.domain.CalendarIntegration;
import com.metafactory.luminabook.domain.enumeration.CalendarProvider;
import com.metafactory.luminabook.repository.CalendarIntegrationRepository;
import com.metafactory.luminabook.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CalendarIntegrationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CalendarIntegrationResourceIT {

    private static final CalendarProvider DEFAULT_PROVIDER = CalendarProvider.GOOGLE;
    private static final CalendarProvider UPDATED_PROVIDER = CalendarProvider.OUTLOOK;

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_CALENDAR_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_CALENDAR_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendar-integrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CalendarIntegrationRepository calendarIntegrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CalendarIntegrationRepository calendarIntegrationRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendarIntegrationMockMvc;

    private CalendarIntegration calendarIntegration;

    private CalendarIntegration insertedCalendarIntegration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarIntegration createEntity() {
        return new CalendarIntegration()
            .provider(DEFAULT_PROVIDER)
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .refreshToken(DEFAULT_REFRESH_TOKEN)
            .externalCalendarId(DEFAULT_EXTERNAL_CALENDAR_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarIntegration createUpdatedEntity() {
        return new CalendarIntegration()
            .provider(UPDATED_PROVIDER)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .externalCalendarId(UPDATED_EXTERNAL_CALENDAR_ID);
    }

    @BeforeEach
    void initTest() {
        calendarIntegration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCalendarIntegration != null) {
            calendarIntegrationRepository.delete(insertedCalendarIntegration);
            insertedCalendarIntegration = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createCalendarIntegration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CalendarIntegration
        var returnedCalendarIntegration = om.readValue(
            restCalendarIntegrationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(calendarIntegration))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CalendarIntegration.class
        );

        // Validate the CalendarIntegration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCalendarIntegrationUpdatableFieldsEquals(
            returnedCalendarIntegration,
            getPersistedCalendarIntegration(returnedCalendarIntegration)
        );

        insertedCalendarIntegration = returnedCalendarIntegration;
    }

    @Test
    @Transactional
    void createCalendarIntegrationWithExistingId() throws Exception {
        // Create the CalendarIntegration with an existing ID
        calendarIntegration.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarIntegrationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        calendarIntegration.setProvider(null);

        // Create the CalendarIntegration, which fails.

        restCalendarIntegrationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendarIntegrations() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        // Get all the calendarIntegrationList
        restCalendarIntegrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarIntegration.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)))
            .andExpect(jsonPath("$.[*].externalCalendarId").value(hasItem(DEFAULT_EXTERNAL_CALENDAR_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalendarIntegrationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(calendarIntegrationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalendarIntegrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(calendarIntegrationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalendarIntegrationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(calendarIntegrationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalendarIntegrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(calendarIntegrationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCalendarIntegration() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        // Get the calendarIntegration
        restCalendarIntegrationMockMvc
            .perform(get(ENTITY_API_URL_ID, calendarIntegration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendarIntegration.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN))
            .andExpect(jsonPath("$.refreshToken").value(DEFAULT_REFRESH_TOKEN))
            .andExpect(jsonPath("$.externalCalendarId").value(DEFAULT_EXTERNAL_CALENDAR_ID));
    }

    @Test
    @Transactional
    void getNonExistingCalendarIntegration() throws Exception {
        // Get the calendarIntegration
        restCalendarIntegrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalendarIntegration() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calendarIntegration
        CalendarIntegration updatedCalendarIntegration = calendarIntegrationRepository.findById(calendarIntegration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCalendarIntegration are not directly saved in db
        em.detach(updatedCalendarIntegration);
        updatedCalendarIntegration
            .provider(UPDATED_PROVIDER)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .externalCalendarId(UPDATED_EXTERNAL_CALENDAR_ID);

        restCalendarIntegrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalendarIntegration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCalendarIntegration))
            )
            .andExpect(status().isOk());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCalendarIntegrationToMatchAllProperties(updatedCalendarIntegration);
    }

    @Test
    @Transactional
    void putNonExistingCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarIntegration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendarIntegrationWithPatch() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calendarIntegration using partial update
        CalendarIntegration partialUpdatedCalendarIntegration = new CalendarIntegration();
        partialUpdatedCalendarIntegration.setId(calendarIntegration.getId());

        partialUpdatedCalendarIntegration.accessToken(UPDATED_ACCESS_TOKEN);

        restCalendarIntegrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarIntegration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalendarIntegration))
            )
            .andExpect(status().isOk());

        // Validate the CalendarIntegration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalendarIntegrationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCalendarIntegration, calendarIntegration),
            getPersistedCalendarIntegration(calendarIntegration)
        );
    }

    @Test
    @Transactional
    void fullUpdateCalendarIntegrationWithPatch() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calendarIntegration using partial update
        CalendarIntegration partialUpdatedCalendarIntegration = new CalendarIntegration();
        partialUpdatedCalendarIntegration.setId(calendarIntegration.getId());

        partialUpdatedCalendarIntegration
            .provider(UPDATED_PROVIDER)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .externalCalendarId(UPDATED_EXTERNAL_CALENDAR_ID);

        restCalendarIntegrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarIntegration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalendarIntegration))
            )
            .andExpect(status().isOk());

        // Validate the CalendarIntegration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalendarIntegrationUpdatableFieldsEquals(
            partialUpdatedCalendarIntegration,
            getPersistedCalendarIntegration(partialUpdatedCalendarIntegration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendarIntegration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendarIntegration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calendarIntegration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarIntegrationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calendarIntegration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarIntegration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendarIntegration() throws Exception {
        // Initialize the database
        insertedCalendarIntegration = calendarIntegrationRepository.saveAndFlush(calendarIntegration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the calendarIntegration
        restCalendarIntegrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendarIntegration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return calendarIntegrationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CalendarIntegration getPersistedCalendarIntegration(CalendarIntegration calendarIntegration) {
        return calendarIntegrationRepository.findById(calendarIntegration.getId()).orElseThrow();
    }

    protected void assertPersistedCalendarIntegrationToMatchAllProperties(CalendarIntegration expectedCalendarIntegration) {
        assertCalendarIntegrationAllPropertiesEquals(
            expectedCalendarIntegration,
            getPersistedCalendarIntegration(expectedCalendarIntegration)
        );
    }

    protected void assertPersistedCalendarIntegrationToMatchUpdatableProperties(CalendarIntegration expectedCalendarIntegration) {
        assertCalendarIntegrationAllUpdatablePropertiesEquals(
            expectedCalendarIntegration,
            getPersistedCalendarIntegration(expectedCalendarIntegration)
        );
    }
}
