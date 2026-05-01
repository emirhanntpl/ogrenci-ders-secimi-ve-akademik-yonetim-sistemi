package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assignment extends BaseEntity {

    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;
}
