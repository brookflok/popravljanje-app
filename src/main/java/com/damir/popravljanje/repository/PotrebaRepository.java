package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Potreba;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Potreba entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotrebaRepository extends JpaRepository<Potreba, Long> {
}
