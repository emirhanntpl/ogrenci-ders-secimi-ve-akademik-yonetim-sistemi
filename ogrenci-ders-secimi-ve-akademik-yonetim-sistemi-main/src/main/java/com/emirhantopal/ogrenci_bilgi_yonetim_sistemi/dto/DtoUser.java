package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {


    @NotEmpty
    private String username;

    @NotEmpty
    private String password;


}
