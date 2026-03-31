package com.metafactory.luminabook.repository;

import com.metafactory.luminabook.domain.CalendarIntegration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CalendarIntegration entity.
 */
@Repository
public interface CalendarIntegrationRepository extends JpaRepository<CalendarIntegration, Long> {
    @Query(
        "select calendarIntegration from CalendarIntegration calendarIntegration where calendarIntegration.user.login = ?#{authentication.name}"
    )
    List<CalendarIntegration> findByUserIsCurrentUser();

    default Optional<CalendarIntegration> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CalendarIntegration> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CalendarIntegration> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select calendarIntegration from CalendarIntegration calendarIntegration left join fetch calendarIntegration.user",
        countQuery = "select count(calendarIntegration) from CalendarIntegration calendarIntegration"
    )
    Page<CalendarIntegration> findAllWithToOneRelationships(Pageable pageable);

    @Query("select calendarIntegration from CalendarIntegration calendarIntegration left join fetch calendarIntegration.user")
    List<CalendarIntegration> findAllWithToOneRelationships();

    @Query(
        "select calendarIntegration from CalendarIntegration calendarIntegration left join fetch calendarIntegration.user where calendarIntegration.id =:id"
    )
    Optional<CalendarIntegration> findOneWithToOneRelationships(@Param("id") Long id);
}
