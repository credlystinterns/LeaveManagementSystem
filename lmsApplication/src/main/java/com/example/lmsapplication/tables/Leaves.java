package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "leaves")
public class Leaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leave_Id;
    @Column(name = "leave_type")
    private String leaveType;
    @Column(name = "leave_status")
    private String status;
    @Column(name = "employee_id")
    private Integer employee_Id ;
    @Column(name = "start_date")
    private Date start_date;
    @Column(name = "end_date")
    private Date end_date;

}
