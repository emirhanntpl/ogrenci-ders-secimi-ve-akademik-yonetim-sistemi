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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseSection> courseSections;


    private String code;

    private String name;

    private String credit;

    private Integer akts;

    private Integer quota;




}
