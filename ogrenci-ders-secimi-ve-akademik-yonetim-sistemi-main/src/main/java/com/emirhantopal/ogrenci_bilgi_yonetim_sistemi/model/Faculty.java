package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import com.fasterxml.jackson.databind.ser.Serializers;
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
public class Faculty extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<Department> departments;


    private  String name;

    private  String address;

    private  String numberOfPersonel;

    private String available;

    private  String year_of_establishment;

    private String dean;

    private String secretary;






}
