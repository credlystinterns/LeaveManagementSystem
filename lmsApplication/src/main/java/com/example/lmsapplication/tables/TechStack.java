package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "techstack")
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer techStackId;
    private String techStackName;
    @OneToOne
    @JoinColumn(name = "employee_id" , nullable = false)
    private Employee employee;
}
