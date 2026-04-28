package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course extends  BaseEntity {

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseSection> courseSections;

    @ManyToOne
    @JoinColumn(name = "department_id") // Yeni eklendi
    private Department department;

    private String code;

    private String name;

    private String credit;

    private Integer akts;

    private Integer quota;
}
