package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.JavnoPitanje;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JavnoPitanje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JavnoPitanjeRepository extends JpaRepository<JavnoPitanje, Long> {
}
