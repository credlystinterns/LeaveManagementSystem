package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "designation")
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer designationId;

    private String designationName;

    @OneToMany(mappedBy = "designation")
    Set<Employee>employeeSet;


}
