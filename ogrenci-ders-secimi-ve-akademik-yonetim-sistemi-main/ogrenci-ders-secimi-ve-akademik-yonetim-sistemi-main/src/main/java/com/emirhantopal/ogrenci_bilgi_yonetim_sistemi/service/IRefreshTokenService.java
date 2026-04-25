package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.RefreshTokenRequest;

public interface IRefreshTokenService {

    public AuthResponse refreshToken(RefreshTokenRequest request);


}
