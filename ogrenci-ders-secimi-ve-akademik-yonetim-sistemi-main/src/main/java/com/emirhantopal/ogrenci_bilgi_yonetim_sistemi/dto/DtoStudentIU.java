package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.BaseEntity;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class DtoStudentIU  extends BaseEntity {

    private String firstName;

    private String lastName;

    private Department department;

    private String studentNumber;

    private String email;

    private String telNumber;






}
