package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "designation")
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designation_id")
    private Integer designationId;
    @Column(name = "designation_name")
    private String designationName;

    @OneToMany(mappedBy = "designation")
    Set<Employee>employeeSet;


}
