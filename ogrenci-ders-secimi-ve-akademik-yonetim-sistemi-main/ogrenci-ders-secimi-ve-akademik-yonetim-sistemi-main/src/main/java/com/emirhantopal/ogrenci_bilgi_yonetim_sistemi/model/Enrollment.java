package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enrollment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;


    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private List<Grade> grades;


    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

    LocalDateTime enrollmentDate;
}
