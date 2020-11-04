package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Lokacija;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Lokacija entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LokacijaRepository extends JpaRepository<Lokacija, Long> {
}
