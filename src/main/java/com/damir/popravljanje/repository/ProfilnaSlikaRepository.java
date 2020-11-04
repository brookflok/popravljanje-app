package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.ProfilnaSlika;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProfilnaSlika entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilnaSlikaRepository extends JpaRepository<ProfilnaSlika, Long> {
}
