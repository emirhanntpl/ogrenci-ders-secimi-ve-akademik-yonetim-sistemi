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
public class Department extends  BaseEntity {

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;


    @OneToMany(mappedBy = "department")
    private List<Student> students;


    @OneToMany(mappedBy = "department")
    private List<Teacher> teachers;


    private  String name;

    private String code;

    private String quota;

    private String head_of_department;













}
