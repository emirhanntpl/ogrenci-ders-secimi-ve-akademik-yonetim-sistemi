package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoStudent extends DtoBaseEntity {
    private String firstName;
    private String lastName;
    private String studentNumber;
    private String email;
    private String telNumber;
    private DtoDepartment department;
    private DtoUser user;
}