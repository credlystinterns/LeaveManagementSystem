package com.example.lmsapplication.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer leave; // leave_id link
    private Integer createdById;
    private LocalDateTime createdAt;

    private Integer updatedById;
    private String updatedAction;
    private LocalDateTime updatedAt;



}
