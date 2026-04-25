package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoTeacherIU extends DtoBaseEntity  {

    private String firstName;

    private String lastName;

    private Long departmentId;

}