package com.metafactory.luminabook.web.rest;

import static com.metafactory.luminabook.domain.BookingPageAsserts.*;
import static com.metafactory.luminabook.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metafactory.luminabook.IntegrationTest;
import com.metafactory.luminabook.domain.BookingPage;
import com.metafactory.luminabook.repository.BookingPageRepository;
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
 * Integration tests for the {@link BookingPageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingPageResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION_MINUTES = 1;
    private static final Integer UPDATED_DURATION_MINUTES = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/booking-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingPageRepository bookingPageRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BookingPageRepository bookingPageRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingPageMockMvc;

    private BookingPage bookingPage;

    private BookingPage insertedBookingPage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingPage createEntity() {
        return new BookingPage()
            .slug(DEFAULT_SLUG)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .durationMinutes(DEFAULT_DURATION_MINUTES)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingPage createUpdatedEntity() {
        return new BookingPage()
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        bookingPage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingPage != null) {
            bookingPageRepository.delete(insertedBookingPage);
            insertedBookingPage = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createBookingPage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingPage
        var returnedBookingPage = om.readValue(
            restBookingPageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingPage.class
        );

        // Validate the BookingPage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBookingPageUpdatableFieldsEquals(returnedBookingPage, getPersistedBookingPage(returnedBookingPage));

        insertedBookingPage = returnedBookingPage;
    }

    @Test
    @Transactional
    void createBookingPageWithExistingId() throws Exception {
        // Create the BookingPage with an existing ID
        bookingPage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingPageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage)))
            .andExpect(status().isBadRequest());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingPage.setSlug(null);

        // Create the BookingPage, which fails.

        restBookingPageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingPage.setTitle(null);

        // Create the BookingPage, which fails.

        restBookingPageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationMinutesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingPage.setDurationMinutes(null);

        // Create the BookingPage, which fails.

        restBookingPageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingPages() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        // Get all the bookingPageList
        restBookingPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingPagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingPageRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingPageRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingPagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingPageRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingPageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingPage() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        // Get the bookingPage
        restBookingPageMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingPage.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.durationMinutes").value(DEFAULT_DURATION_MINUTES))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingBookingPage() throws Exception {
        // Get the bookingPage
        restBookingPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingPage() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingPage
        BookingPage updatedBookingPage = bookingPageRepository.findById(bookingPage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingPage are not directly saved in db
        em.detach(updatedBookingPage);
        updatedBookingPage
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE);

        restBookingPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookingPage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBookingPage))
            )
            .andExpect(status().isOk());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingPageToMatchAllProperties(updatedBookingPage);
    }

    @Test
    @Transactional
    void putNonExistingBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingPage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingPage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingPageWithPatch() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingPage using partial update
        BookingPage partialUpdatedBookingPage = new BookingPage();
        partialUpdatedBookingPage.setId(bookingPage.getId());

        partialUpdatedBookingPage.slug(UPDATED_SLUG).description(UPDATED_DESCRIPTION).durationMinutes(UPDATED_DURATION_MINUTES);

        restBookingPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingPage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingPage))
            )
            .andExpect(status().isOk());

        // Validate the BookingPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingPageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingPage, bookingPage),
            getPersistedBookingPage(bookingPage)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingPageWithPatch() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingPage using partial update
        BookingPage partialUpdatedBookingPage = new BookingPage();
        partialUpdatedBookingPage.setId(bookingPage.getId());

        partialUpdatedBookingPage
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE);

        restBookingPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingPage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingPage))
            )
            .andExpect(status().isOk());

        // Validate the BookingPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingPageUpdatableFieldsEquals(partialUpdatedBookingPage, getPersistedBookingPage(partialUpdatedBookingPage));
    }

    @Test
    @Transactional
    void patchNonExistingBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingPage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPageMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingPage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingPage() throws Exception {
        // Initialize the database
        insertedBookingPage = bookingPageRepository.saveAndFlush(bookingPage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingPage
        restBookingPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingPage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingPageRepository.count();
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

    protected BookingPage getPersistedBookingPage(BookingPage bookingPage) {
        return bookingPageRepository.findById(bookingPage.getId()).orElseThrow();
    }

    protected void assertPersistedBookingPageToMatchAllProperties(BookingPage expectedBookingPage) {
        assertBookingPageAllPropertiesEquals(expectedBookingPage, getPersistedBookingPage(expectedBookingPage));
    }

    protected void assertPersistedBookingPageToMatchUpdatableProperties(BookingPage expectedBookingPage) {
        assertBookingPageAllUpdatablePropertiesEquals(expectedBookingPage, getPersistedBookingPage(expectedBookingPage));
    }
}
