package com.businessapi.repository;

import com.businessapi.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveSupportRepository extends JpaRepository<ChatMessage, Long> {
}
