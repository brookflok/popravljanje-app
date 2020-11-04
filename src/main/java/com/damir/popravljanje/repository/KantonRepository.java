package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Kanton;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Kanton entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KantonRepository extends JpaRepository<Kanton, Long> {
}
