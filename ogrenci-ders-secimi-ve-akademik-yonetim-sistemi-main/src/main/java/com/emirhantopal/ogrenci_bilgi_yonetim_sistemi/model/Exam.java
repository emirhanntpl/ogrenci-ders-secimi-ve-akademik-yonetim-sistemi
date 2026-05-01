package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exam extends BaseEntity {

    private String examName; // Vize, Final, Quiz
    
    private LocalDateTime examDate;
    
    private String classroom;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;
}
