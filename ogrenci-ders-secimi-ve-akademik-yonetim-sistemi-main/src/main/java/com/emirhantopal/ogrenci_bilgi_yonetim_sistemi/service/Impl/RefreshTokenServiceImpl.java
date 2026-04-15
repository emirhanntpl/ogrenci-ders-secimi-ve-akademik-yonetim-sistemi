package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.JwtService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.RefreshTokenRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.RefreshToken;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.RefreshTokenRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtService jwtService;


    private boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().before(expiredDate);
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() +1000*60*60*12));
        refreshToken.setUser(user);

        return  refreshToken;
    }




    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> OptToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (OptToken.isEmpty()){
            System.out.println("Token geçersizdir. Lütfen tekrar deneyiniz" + request.getRefreshToken());
        }
        RefreshToken refreshToken = OptToken.get();
        if (!isRefreshTokenExpired(refreshToken.getExpireDate())){
            System.out.println("Refresh Token süresi dolmuştur, yenileyiniz." + request.getRefreshToken());
        }

        String accessToken = jwtService.generateToken(refreshToken.getUser());
        RefreshToken saveRefreshToken = refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));

        return  new AuthResponse(accessToken,saveRefreshToken.getRefreshToken());
    }
}
