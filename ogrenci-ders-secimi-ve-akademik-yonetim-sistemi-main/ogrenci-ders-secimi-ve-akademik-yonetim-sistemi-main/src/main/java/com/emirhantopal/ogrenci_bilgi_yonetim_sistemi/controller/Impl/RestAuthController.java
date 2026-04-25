package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IRestAuthController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.RefreshTokenRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAuthService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IRefreshTokenService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.RefreshTokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAuthController implements IRestAuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IRefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Override
    public DtoUser register(@Valid @RequestBody AuthRequest request) {
        return authService.register(request) ;
    }
    
    @PostMapping("/registerAdmin")
    @Override
    public DtoUser registerAdmin(@Valid @RequestBody AuthRequest request) {
        return authService.registerAdmin(request) ;
    }

    @PostMapping("/authenticate")
    @Override
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refreshToken")
    @Override
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request) ;
    }


}