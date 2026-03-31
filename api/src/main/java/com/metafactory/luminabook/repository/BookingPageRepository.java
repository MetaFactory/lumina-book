package com.metafactory.luminabook.repository;

import com.metafactory.luminabook.domain.BookingPage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingPage entity.
 */
@Repository
public interface BookingPageRepository extends JpaRepository<BookingPage, Long> {
    @Query("select bookingPage from BookingPage bookingPage where bookingPage.user.login = ?#{authentication.name}")
    List<BookingPage> findByUserIsCurrentUser();

    default Optional<BookingPage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingPage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingPage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingPage from BookingPage bookingPage left join fetch bookingPage.user",
        countQuery = "select count(bookingPage) from BookingPage bookingPage"
    )
    Page<BookingPage> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookingPage from BookingPage bookingPage left join fetch bookingPage.user")
    List<BookingPage> findAllWithToOneRelationships();

    @Query("select bookingPage from BookingPage bookingPage left join fetch bookingPage.user where bookingPage.id =:id")
    Optional<BookingPage> findOneWithToOneRelationships(@Param("id") Long id);
}
