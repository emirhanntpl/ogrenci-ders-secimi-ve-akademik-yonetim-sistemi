package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class Semester  extends  BaseEntity {


    private String term;

    Boolean isActive;






}
