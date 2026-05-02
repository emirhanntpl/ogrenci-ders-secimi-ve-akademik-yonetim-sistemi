package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Grade extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    private String examName; // Vize, Final, Ödev vb.

    private  Double midTerm;

    private  Double finalExam;

    private Double average;

    private String letterGrade; // Yeni eklendi
    private String passStatus;  // Yeni eklendi
}
