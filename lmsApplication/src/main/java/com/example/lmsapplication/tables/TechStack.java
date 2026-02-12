package com.example.lmsapplication.tables;

import aQute.bnd.annotation.headers.BundleLicense;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tech_stack")
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "tech_stack_id")
    private Integer techStackId;

    @Column(name = "tech_stack_name")
    private String techStackName;

}
