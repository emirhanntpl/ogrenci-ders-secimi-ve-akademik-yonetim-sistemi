package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;


import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.RefreshTokenRequest;
import org.springframework.web.bind.annotation.RestController;


public interface IRestAuthController {

    public DtoUser register(AuthRequest request);
    
    public DtoUser registerAdmin(AuthRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public AuthResponse refreshToken(RefreshTokenRequest request);


}