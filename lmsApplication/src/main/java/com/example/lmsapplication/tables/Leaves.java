package com.example.lmsapplication.tables;

import com.example.lmsapplication.enums.LeaveStatus;
import com.example.lmsapplication.enums.LeaveTypes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;



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
    private Integer id;
    @Column(name = "leave_type")
    @Enumerated(EnumType.STRING)
    private LeaveTypes leaveType;
    @Column(name = "leave_status")
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
    @Column(name = "employee_id")
    private Integer employee ;

    @Column(name = "start_date")
    private LocalDate startDate;


    @Column(name = "end_date")
    private LocalDate endDate;

}
