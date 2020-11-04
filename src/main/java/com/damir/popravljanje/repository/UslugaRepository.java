package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Usluga;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Usluga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UslugaRepository extends JpaRepository<Usluga, Long> {
}
