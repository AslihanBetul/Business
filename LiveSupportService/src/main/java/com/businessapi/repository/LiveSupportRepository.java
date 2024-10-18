package com.businessapi.repository;

import com.businessapi.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveSupportRepository extends JpaRepository<ChatMessage, Long> {


}
