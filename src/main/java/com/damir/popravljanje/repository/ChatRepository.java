package com.damir.popravljanje.repository;

import com.damir.popravljanje.domain.Chat;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Chat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
