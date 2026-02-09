package com.example.lmsapplication.tables;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sessions")
public class Session {
    @Id
    private Integer employeeId;
    private String  sessionToken;

}
