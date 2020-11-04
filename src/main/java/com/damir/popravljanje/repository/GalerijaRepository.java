package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Galerija;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Galerija entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GalerijaRepository extends JpaRepository<Galerija, Long> {
}
