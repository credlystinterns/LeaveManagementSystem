package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "designation")
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer designationId;

    private String designationName;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
