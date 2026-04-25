package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
