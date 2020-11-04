package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Poruka;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Poruka entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PorukaRepository extends JpaRepository<Poruka, Long> {
}
