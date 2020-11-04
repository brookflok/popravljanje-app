package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Entiteti;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Entiteti entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntitetiRepository extends JpaRepository<Entiteti, Long> {
}
