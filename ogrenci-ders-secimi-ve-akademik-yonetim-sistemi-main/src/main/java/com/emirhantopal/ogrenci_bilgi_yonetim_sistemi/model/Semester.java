package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "semesters")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Semester extends BaseEntity {

    private String term; // Örn: "2024-2025 Güz"

    private Boolean isActive;

    @OneToMany(mappedBy = "semester")
    private List<CourseSection> courseSections;
}