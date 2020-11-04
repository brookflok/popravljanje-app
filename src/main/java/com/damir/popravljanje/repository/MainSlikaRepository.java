package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.MainSlika;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MainSlika entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainSlikaRepository extends JpaRepository<MainSlika, Long> {
}
