package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser extends DtoBaseEntity {
    private String username;
    // Rolleri veya diğer alanları buraya ekleyebilirsiniz
}